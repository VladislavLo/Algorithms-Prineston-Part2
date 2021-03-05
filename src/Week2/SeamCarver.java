package Week2;
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture _picture;
    private final int BORDER_ENERGY = 1000;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        _picture = picture;
    }

    // current picture
    public Picture picture() {
        return _picture;
    }

    // width of current picture
    public int width() {
        return _picture.width();
    }

    // height of current picture
    public int height() {
        return _picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
            throw new IllegalArgumentException("Indices are out of boundary");
        }
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return BORDER_ENERGY;
        }
        return Math.sqrt(getDeltaX(x, y) + getDeltaY(x, y));
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int height = height();
        int width = width();
        double[][] energies = calculateEnergies(height, width);

        int[][] edgeTo = new int[height][width];
        double[][] sum = new double[2][width];
        sum[0] = energies[0];
        for (int currentHeight = 1; currentHeight < height; currentHeight++) {
            double[] currentRow = energies[currentHeight];
            for (int i = 0; i < width; i++) {
                int left = Math.max(0, i - 1);
                int right = Math.min(i + 1, width - 1);
                double minVertex = Double.MAX_VALUE;
                int minIndex = 0;
                for (int k = left; k <= right; k++) {
                    if (sum[0][k] < minVertex) {
                        minVertex = sum[0][k];
                        minIndex = k;
                    }
                }
                edgeTo[currentHeight][i] = minIndex;
                sum[1][i] = sum[0][minIndex] + currentRow[i];
            }
            for (int i = 0; i < width; i++) {
                sum[0][i] = sum[1][i];
                sum[1][i]= 0;
            }
        }

        double minSum = Double.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < width; i++) {
            if (sum[0][i] < minSum) {
                minSum = sum[0][i];
                minIndex = i;
            }
        }

        int[] path = new int[height];
        int fromHeight = height - 1;
        path[fromHeight] = minIndex;
        while (fromHeight > 0) {
            int lastIndex = path[fromHeight];
            int elem = edgeTo[fromHeight][lastIndex];
            path[--fromHeight] = elem;
        }
        return path;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int height = height();
        int width = width();
        double[][] energies = calculateEnergies(height, width);

        int[][] edgeTo = new int[height][width];
        double[][] sum = new double[2][height];
        for (int i = 0; i < height; i++) {
            sum[0][i] = BORDER_ENERGY;
        }
        for (int currentColumn = 1; currentColumn < width; currentColumn++) {
            for (int currentRow = 0; currentRow < height; currentRow++) {
                int upper = Math.max(0, currentRow - 1);
                int down = Math.min(currentRow + 1, height - 1);
                double minVertex = Double.MAX_VALUE;
                int minIndex = 0;
                for (int k = upper; k <= down; k++) {
                    if (sum[0][k] < minVertex) {
                        minVertex = sum[0][k];
                        minIndex = k;
                    }
                }
                edgeTo[currentRow][currentColumn] = minIndex;
                sum[1][currentRow] = sum[0][minIndex] + energies[currentRow][currentColumn];
            }
            for (int i = 0; i < height; i++) {
                sum[0][i] = sum[1][i];
                sum[1][i]= 0;
            }
        }

        double minSum = Double.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < height; i++) {
            if (sum[0][i] < minSum) {
                minSum = sum[0][i];
                minIndex = i;
            }
        }

        int[] path = new int[width];
        int fromWidth = width - 1;
        path[fromWidth] = minIndex;
        while (fromWidth > 0) {
            int lastIndex = path[fromWidth];
            int elem = edgeTo[lastIndex][fromWidth];
            path[--fromWidth] = elem;
        }
        return path;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width()) {
            throw new IllegalArgumentException("Seam is illegal.");
        }

        if (height() <= 1) {
            throw new IllegalArgumentException("height of the picture is less than or equal to 1");
        }

        int w = width();
        int h = height() - 1;

        Picture carved = new Picture(w, h);
        for (int column = 0; column < carved.width(); column++) {

            for (int row = 0; row < seam[column]; row++) {
                carved.setRGB(column, row, _picture.getRGB(column, row));
            }

            for (int row = seam[column]; row < carved.height(); row++) {
                carved.setRGB(column, row, _picture.getRGB(column, row + 1));
            }
        }

        _picture = carved;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height()) {
            throw new IllegalArgumentException("Seam is illegal.");
        }

        if (width() <= 1) {
            throw new IllegalArgumentException("Width of the picture is less than or equal to 1");
        }

        int w = width() - 1;
        int h = height();

        Picture carved = new Picture(w, h);
        for (int y = 0; y < carved.height(); y++) {

            for (int x = 0; x < seam[y]; x++) {
                carved.setRGB(x, y, _picture.getRGB(x, y));
            }

            for (int x = seam[y]; x < carved.width(); x++) {
                carved.setRGB(x, y, _picture.getRGB(x + 1, y));
            }
        }

        _picture = carved;
    }

    private int getDeltaX(int x, int y)
    {
        Color color1 = _picture.get(x - 1, y);
        Color color2 = _picture.get(x + 1, y);
        int deltaR = color2.getRed() - color1.getRed();
        int deltaB = color2.getBlue() - color1.getBlue();
        int deltaG = color2.getGreen() - color1.getGreen();
        return (deltaR * deltaR) + (deltaB * deltaB) + (deltaG * deltaG);
    }

    private int getDeltaY(int x, int y)
    {
        Color color1 = _picture.get(x, y - 1);
        Color color2 = _picture.get(x, y + 1);
        int deltaR = color2.getRed() - color1.getRed();
        int deltaB = color2.getBlue() - color1.getBlue();
        int deltaG = color2.getGreen() - color1.getGreen();
        return (deltaR * deltaR) + (deltaB * deltaB) + (deltaG * deltaG);
    }

    private double[][] calculateEnergies(int height, int width) {
        double[][] energies = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                energies[i][j] = energy(j, i);
            }
        }
        return energies;
    }
}
