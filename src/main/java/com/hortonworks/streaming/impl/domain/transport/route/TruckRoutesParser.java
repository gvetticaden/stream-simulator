package com.hortonworks.streaming.impl.domain.transport.route;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.hortonworks.streaming.impl.domain.gps.Location;
import com.hortonworks.streaming.impl.domain.transport.route.jaxb.Kml;
import com.hortonworks.streaming.impl.domain.transport.route.jaxb.Placemark;



public class TruckRoutesParser {
	
	private static final Logger LOG = Logger.getLogger(TruckRoutesParser.class);

	public Route parseRoute(String routeFile) {
        LOG.info("Processing Route File["+routeFile+"]");
		Route route = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(Kml.class);
			Unmarshaller u = jc.createUnmarshaller();
			Source source = new StreamSource(new FileInputStream(routeFile));
			
			JAXBElement<Kml> root =  u.unmarshal(source, Kml.class );
			
			Kml kml = root.getValue();
			
			String routeName = kml.getDocument().getName();
			List<Location> locations = new ArrayList<Location>();
			//-74.1346263885498,40.63616666172068,0.0
			for(Placemark placemark:kml.getDocument().getPlacemark()) {
				String coordinates = placemark.getPoint().getCoordinates();
				String[] coord = coordinates.split(",");
				locations.add(new Location(Double.valueOf(coord[0]), Double.valueOf(coord[1]), 0));
			}
			LOG.info("Route File["+routeFile +"] has " + locations.size() + " coordinates in the route "); 
			route = new RouteProvided(routeName, locations);
		} catch (FileNotFoundException e) {
			String errorMessage = "Error Opening routeFile["+routeFile+"]";
			LOG.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		} catch (JAXBException e) {
			String errorMessage = "JaxB exception for routeFile"+routeFile+"]";
			LOG.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		}
		return route;
	}
	
	public List<Route> parseAllRoutes(String directoryName) {
		List<Route> routes = new ArrayList<>();
		File file = new File(directoryName);
		for(File routeFile: file.listFiles()) {
			if(routeFile.getPath().endsWith(".xml")) {
				routes.add(parseRoute(routeFile.getPath()));
			}
		}
		return routes;
	}
}
