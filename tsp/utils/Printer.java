package utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * 
 * @author Marek Arnold (arnd@zhaw.ch)
 *
 * Use this class to generate visual representations of a solution.
 */
public class Printer {
	
	/**
	 * Export an instance and it's corresponding solution to a HTML file containing an SVG representing the solution.
	 * @param instance
	 * @param solution 
	 * @param filePath Path to the file to write to.
	 * @throws IOException
	 */
	public static void writeToSVG(Instance instance, List<Point> solution, Path filePath) throws IOException{
		double margin = 100;
		
		double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = 0, maxY = 0;
		
		for(Point point : solution){
			maxX = Math.max(maxX, point.getX());
			maxY = Math.max(maxY, point.getY());
			
			minX = Math.min(minX, point.getX());
			minY = Math.min(minY, point.getY());
		}

		double xTransform = margin - minX;
		double yTransform = margin - minY;
		
		int height = (int)(maxY + margin + yTransform), width = (int)(maxX + xTransform + margin);

		Files.createDirectories(filePath.getParent());
		try(BufferedWriter svgWriter = Files.newBufferedWriter(filePath)){
			svgWriter.write("<html>");
			svgWriter.write("<head>");
			svgWriter.write("<style>");
			svgWriter.write(".hoverbg {background:rgba(255,255,255,0.3);}");
			svgWriter.write(".hoverbg:hover {background:rgba(255,255,255,1.0);}");
			svgWriter.write("</style>");
			svgWriter.write("<title>" + instance.getName() + "</title>");
			svgWriter.write("</head>");
			svgWriter.write("<body>");
			svgWriter.write("<div class=\"hoverbg\" style=\"position:absolute; top:10;left:10; z-index:100;\">");
			svgWriter.write("<p style=\"font-size:30px\">" + instance.getName() + "</p>");
			svgWriter.write("<p style=\"font-size:15px\">" + instance.getComment() + "</p>");
			svgWriter.write("<p style=\"font-size:30px\">Total distance: " + ((int)Math.rint(Utils.euclideanDistance2D(solution) * 10) / 10.0) + "</p>");
			svgWriter.write("</div>");
			svgWriter.write("<svg viewBox=\"0 0 " + width + " " + height + "\" style=\"position:absolute; top:0; left:0; bottom:0; right:0; z-index:1;\">");
			
			for(Point point : solution){
				svgWriter.write("<circle cx=\"" + (int)Math.rint(point.getX() + xTransform) + "\" cy=\"" + (int)Math.rint(point.getY() + yTransform) + "\" r=\"10\" stroke=\"black\" stroke-width=\"1\" fill=\"black\"/>");
			}
			
			Point currentPoint = solution.get(0);
			
			for (int i = 1; i < solution.size(); i++){
				Point nextPoint = solution.get(i);
				
				writeSVGLine(currentPoint, nextPoint, svgWriter, xTransform, yTransform);
				
				currentPoint = nextPoint;
			}
			
			writeSVGLine(currentPoint, solution.get(0), svgWriter, xTransform, yTransform);
			
			svgWriter.write("</svg>");
			svgWriter.write("</body>");
			svgWriter.write("</html>");
		}
	}
	
	private static void writeSVGLine(Point a, Point b, BufferedWriter svgWriter, double xTransform, double yTransform) throws IOException{
		svgWriter.write("<line x1=\"" + (int)Math.rint(a.getX() + xTransform) + "\" y1=\"" + (int)Math.rint(a.getY() + yTransform) + "\" x2=\"" + (int)Math.rint(b.getX() + xTransform) + "\" y2=\"" + (int)Math.rint(b.getY() + yTransform) + "\" style=\"stroke:rgb(255,0,0);stroke-width:5\"/>");
	}
}
