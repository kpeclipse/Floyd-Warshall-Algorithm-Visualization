import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GraphReader {
    private Graph graph;
    private File file;
    private ArrayList<Integer> xBounds = new ArrayList<Integer>();
    private ArrayList<Integer> yBounds = new ArrayList<Integer>();

    public GraphReader(Window w, File f) {
        graph = w.graph;
        file = f;
        setCoordinates();
        readGraph();
    }

    public void setCoordinates() { // possible coordinates for random vertices
        int multiple = 30;
        for (int i = 0; i < 17; i++) {
            xBounds.add(multiple);
            yBounds.add(multiple);
            multiple += 30;
        }

        for (int i = 17; i < 22; i++) {
            yBounds.add(multiple);
            multiple += 30;
        }
    }

    public void readGraph() {
        boolean readVertex = false;
        boolean readEdge = false;
        String line;
        int numberOfVertices = 0;
        Random r = new Random();
        int setX = 0;
        int setY = 0;

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                if (line.charAt(0) == '/')
                    continue;

                if (line.contains("VERTICES:")) {
                    readVertex = true;
                    continue;
                }

                if (line.contains("EDGES:")) {
                    readEdge = true;
                    readVertex = false;
                    continue;
                }

                if (readVertex) {
                    setX = xBounds.get(r.nextInt(17));
                    setY = yBounds.get(r.nextInt(22));

                    if (graph.vertices != null) {
                        boolean overlap = true;
                        while (overlap) {
                            overlap = checkVertex(setX, setY); // check if there is existing vertex on point

                            if (overlap) {
                                setX = xBounds.get(r.nextInt(17));
                                setY = yBounds.get(r.nextInt(22));
                            }
                        }
                    }

                    graph.addVertex(line, setX, setY);
                    numberOfVertices = graph.vertices.size();
                }

                if (readEdge) {
                    String[] details = line.split(" ", 3);
                    graph.addEdge(details[0], details[1], Double.parseDouble(details[2]));
                }
            }

            bufferedReader.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public boolean checkVertex(int currentX, int currentY) {
        for (int i = 0; i < graph.vertices.size(); i++)
            if (graph.vertices.get(i).getX() == currentX && graph.vertices.get(i).getY() == currentY)
                return true;

        return false;
    }

    public Graph getGraph() {
        return graph;
    }
}