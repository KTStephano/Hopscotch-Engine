package engine.math;

public class Matrix3 {
    /**
     * The format for this is as follows:
     *      row0 indices: 0,1,2
     *      row1 indices: 3,4,5
     *      row2 indices: 6,7,8
     */
    private final double _mat[] = new double[9];

    public Matrix3()
    {
    }

    /**
     * Sets all elements of the matrix
     * @param val value to set all matrix elements to
     */
    public void set(double val)
    {
        for (int i = 0; i < _mat.length; ++i) _mat[i] = val;
    }
}
