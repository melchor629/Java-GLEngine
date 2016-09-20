package org.melchor629.engine.utils.math;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 * Generic abstract class for Matrices
 */
public class Matrix<T extends Number> implements Cloneable {
    protected T[] storage;
    protected int cols, rows;
    protected ArithmeticOperations<T> ops;
    private T det = null;

    /**
     * Creates a new matrix of rows x cols with the given operations over numbers.
     * The matrix is set to the identity if is squared, or with all 0 otherwise.
     * @param cols number of columns
     * @param rows number of rows
     * @param ops operations over numbers
     */
    @SuppressWarnings("unchecked")
    public Matrix(int cols, int rows, ArithmeticOperations<T> ops) {
        this.cols = cols;
        this.rows = rows;
        this.ops = ops;
        storage = (T[]) new Number[cols * rows];

        if(cols == rows) {
            setIdentity();
        } else {
            for(int i = 0; i < storage.length; i++) {
                storage[i] = ops.convert(0);
            }
        }
    }

    protected int index(int col, int row) {
        return (col - 1) * rows + row - 1;
    }

    protected T valueAt(int col, int row) {
        return storage[index(col, row)];
    }

    protected void setvalueAt(int col, int row, T value) {
        storage[index(col, row)] = value;
        det = null;
    }

    /**
     * Retreives the value at M<sub>row,col</sub>. Row and Col indexes starts at
     * position 1
     * @param row row index
     * @param col column index
     * @return the value at M<sub>row,col</sub>
     * @throws IndexOutOfBoundsException if the indexes are not valid
     */
    public T getValueAt(int row, int col) {
        if(col > cols && row > rows || col < 0 || row < 0)
            throw new IndexOutOfBoundsException("Invalid index (" + col + "," + row + ")");
        return storage[index(col, row)];
    }

    /**
     * Sets a value at M<sub>row,col</sub>. Row and Col indexes starts at
     * position 1
     * @param row row index
     * @param col column index
     * @param value the value to set
     * @throws IndexOutOfBoundsException if the indexes are not valid
     */
    public void setValueAt(int row, int col, T value) {
        if(col > cols && row > rows || col < 0 || row < 0)
            throw new IndexOutOfBoundsException("Invalid index (" + col + "," + row + ")");
        storage[index(col, row)] = value;
        det = null;
    }

    /**
     * @return the number of columns in the matrix
     */
    public int getColumns() {
        return cols;
    }

    /**
     * @return the number of rows in the matrix
     */
    public int getRows() {
        return rows;
    }

    /**
     * Fills the matrix with values ordered like this:<br>
     * M<sub>1,1</sub>, M<sub>1,2</sub>, ⋯ M<sub>1,m</sub>,<br>
     * M<sub>2,1</sub>, M<sub>2,2</sub>, ⋯ M<sub>2,m</sub>,<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;⋮
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;⋮
     * &nbsp;&nbsp;&nbsp;&nbsp;⋱
     * &nbsp;&nbsp;&nbsp;⋮<br>
     * M<sub>n,1</sub>, M<sub>n,2</sub>, ⋯ M<sub>n,m</sub><br>
     * Collection must have at least {@link #getColumns()} * {@link #getRows()}
     * values on it
     * @param values values ordered like above
     * @return this matrix
     * @throws IllegalArgumentException If has not enough values to fill
     */
    public Matrix<T> fillWithValues(Collection<T> values) {
        if(values.size() < storage.length)
            throw new IllegalArgumentException("Collection has less values than needed");

        Iterator<T> iterator = values.iterator();
        for(int row = 1; row <= rows; row++) {
            for(int col = 1; col <= cols; col++) {
                setValueAt(row, col, iterator.next());
            }
        }

        det = null;
        return this;
    }

    /**
     * Fills the matrix with values ordered like this:<br>
     * M<sub>1,1</sub>, M<sub>1,2</sub>, ⋯ M<sub>1,m</sub>,<br>
     * M<sub>2,1</sub>, M<sub>2,2</sub>, ⋯ M<sub>2,m</sub>,<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;⋮
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;⋮
     * &nbsp;&nbsp;&nbsp;&nbsp;⋱
     * &nbsp;&nbsp;&nbsp;⋮<br>
     * M<sub>n,1</sub>, M<sub>n,2</sub>, ⋯ M<sub>n,m</sub><br>
     * Array must have at least {@link #getColumns()} * {@link #getRows()}
     * values on it
     * @param values values ordered like above
     * @return this matrix
     * @throws IllegalArgumentException If has not enough values to fill
     */
    @SafeVarargs
    public final Matrix<T> fillWithValues(T... values) {
        if(values.length < storage.length)
            throw new IllegalArgumentException("Number of values is less than needed");

        int i = 0;
        for(int row = 1; row <= rows; row++) {
            for(int col = 1; col <= cols; col++) {
                setValueAt(row, col, values[i++]);
            }
        }

        det = null;
        return this;
    }

    /**
     * Fills the matrix with the same value
     * @param value value to be set in all items
     * @return itself
     */
    public Matrix<T> fillWithValue(T value) {
        return apply((n) -> value);
    }

    public Matrix<T> setIdentity() {
        if(cols != rows)
            throw new ArithmeticException("Is not possible set matrix to identity");

        for(int row = 1; row <= rows; row++) {
            for(int col = 1; col <= cols; col++) {
                if(row == col) {
                    setValueAt(row, col, ops.convert(1));
                } else {
                    setValueAt(row, col, ops.convert(0));
                }
            }
        }

        det = null;
        return this;
    }

    /**
     * Adds this matrix to another one, and the result is stored on this matrix.
     * The number of columns and rows must match to be able to operate.
     * @param matrix the other matrix
     * @return itself
     * @throws ArithmeticException if matrices have different dimensions
     */
    public Matrix<T> add(Matrix<T> matrix) {
        if(cols != matrix.cols || rows != matrix.rows)
            throw new ArithmeticException("Sum with different dimension matrices");

        for(int col = 1; col <= cols; col++) {
            for(int row = 1; row <= rows; row++) {
                T value = ops.add(valueAt(col, row), matrix.valueAt(col, row));
                setvalueAt(col, row, value);
            }
        }

        det = null;
        return this;
    }

    /**
     * Adds this matrix to another one, and the result is stored on this matrix.
     * The number of columns and rows must match to be able to operate.
     * @param matrix the other matrix
     * @return itself
     * @throws ArithmeticException if matrices have different dimensions
     */
    public Matrix<T> substract(Matrix<T> matrix) {
        if(cols != matrix.cols || rows != matrix.rows)
            throw new ArithmeticException("Substract with different dimension matrices");

        for(int col = 1; col <= cols; col++) {
            for(int row = 1; row <= rows; row++) {
                T value = ops.sub(valueAt(col, row), matrix.valueAt(col, row));
                setvalueAt(col, row, value);
            }
        }

        det = null;
        return this;
    }

    /**
     * Multiplies this matrix to another one, and the result is stored on this matrix.
     * The number of columns of this matrix must match the number of rows of
     * the other matrix to be able to operate.
     * @param matrix the other matrix
     * @return itself
     * @throws ArithmeticException if matrices have different dimensions
     */
    @SuppressWarnings("unchecked")
    public Matrix<T> multiply(Matrix<T> matrix) {
        if(cols != matrix.rows)
            throw new ArithmeticException("Multply with not valid dimension matrices");

        T[] tmpStorage = (T[]) new Number[storage.length];

        for(int row = 1; row <= cols; row++) {
            for(int col = 1; col <= rows; col++) {
                T sum = null;
                for(int j = 1; j <= cols; j++) {
                    T value = ops.mul(valueAt(j, row), matrix.valueAt(col, j));
                    sum = sum == null ? value : ops.add(sum, value);
                }
                tmpStorage[index(col, row)] = sum;
            }
        }

        System.arraycopy(tmpStorage, 0, storage, 0, storage.length);

        det = null;
        return this;
    }

    /**
     * Multiplies this matrix with the scalar.
     * @param scalar scalar to multiply
     * @return itself
     */
    public Matrix<T> multiply(T scalar) {
        for(int col = 1; col <= cols; col++) {
            for(int row = 1; row <= rows; row++) {
                T value = ops.mul(valueAt(col, row), scalar);
                setvalueAt(col, row, value);
            }
        }

        det = null;
        return this;
    }

    /**
     * Calculates the inverse matrix and saves it into this matrix. Inverse
     * matrix is only calculable if is a square matrix (same number of rows and
     * columns) and its determinant is diferent than 0.
     * @return itself
     */
    public Matrix<T> invert() {
        if(!isSquare())
            throw new ArithmeticException("Invert on matrix not squared");
        if(GLM.floatEquals(determinant().doubleValue(), 0.0, 0.001))
            throw new ArithmeticException("Determinant is 0, inverse don't exist");

        Matrix<T> inv = clone();

        if(cols == 1) {
            inv.setValueAt(1, 1, storage[0]);
        } else {
            for(int row = 1; row <= rows; row++) {
                for(int col = 1; col <= cols; col++) {
                    inv.setValueAt(row, col, adjunct(row, col).determinant());
                }
            }
        }

        inv.transpose().multiply(ops.div(ops.convert(1), determinant()));
        System.arraycopy(inv.storage, 0, storage, 0, storage.length);

        det = null;
        return this;
    }

    /**
     * Calcules the determinant of this matrix
     * @return matrix's determinant
     */
    public T determinant() {
        if(!isSquare())
            throw new ArithmeticException("Determinant on non square Matrix");

        if(det != null) return det;

        if(cols == 1) {
            return this.det = storage[0];
        } else if(cols == 2) {
            return this.det = ops.sub(
                ops.mul(valueAt(1, 1), valueAt(2, 2)),
                ops.mul(valueAt(1, 2), valueAt(2, 1)));
        } else if(cols == 3) {
            T a1 = ops.mul(ops.mul(valueAt(1, 1), valueAt(2, 2)), valueAt(3, 3));
            T a2 = ops.mul(ops.mul(valueAt(1, 2), valueAt(3, 1)), valueAt(2, 3));
            T a3 = ops.mul(ops.mul(valueAt(1, 3), valueAt(2, 1)), valueAt(3, 2));
            T b1 = ops.mul(ops.mul(valueAt(1, 3), valueAt(2, 2)), valueAt(3, 1));
            T b2 = ops.mul(ops.mul(valueAt(1, 2), valueAt(2, 1)), valueAt(3, 3));
            T b3 = ops.mul(ops.mul(valueAt(1, 1), valueAt(3, 2)), valueAt(2, 3));
            T a = ops.add(ops.add(a1, a2), a3);
            T b = ops.add(ops.add(b1, b2), b3);
            return this.det = ops.sub(a, b);
        } else {
            Matrix<T> opt = clone();
            Vector<T> firstRow = opt.getRow(1);

            for(int row = 2; row <= rows; row++) {
                Vector<T> row_ = opt.getRow(row);
                T modifier = ops.div(firstRow.getCoord(1), row_.getCoord(1));
                modifier = ops.mul(modifier, ops.convert(-1f));
                setRow(row, row_.product(modifier).add(firstRow));
            }

            return this.det = adjunct(1, 1).determinant();
        }
    }

    /**
     * Transposes the matrix. All rows become columns and columns become rows
     * @return itself
     */
    @SuppressWarnings("unchecked")
    public Matrix<T> transpose() {
        T[] tmpStorage = (T[]) new Number[storage.length];

        for(int row = 1; row <= rows; row++) {
            for(int col = 1; col <= cols; col++) {
                T a = valueAt(col, row);
                tmpStorage[(row - 1) * cols + col - 1] = a;
            }
        }

        System.arraycopy(tmpStorage, 0, storage, 0, storage.length);
        int tmp = cols;
        cols = rows;
        rows = tmp;
        det = null;
        return this;
    }

    /**
     * Calculates the complementary minor of an element of this matrix.
     * The complementary minor is a submatrix that is floatEquals to this one, but
     * lacks {@code row} row and {@code col} column
     * @param row row of the element
     * @param col column of the element
     * @return complementary minor of the element
     */
    public Matrix<T> complementaryMinor(int row, int col) {
        if(col > cols && row > rows || col < 0 || row < 0)
            throw new IndexOutOfBoundsException("Invalid index (" + col + "," + row + ")");
        if(cols < 2 || rows < 2)
            throw new ArithmeticException("Not enough colums or rows in matrix");

        Matrix<T> compl = new Matrix<>(cols-1, rows-1, ops);

        for(int i = 1; i <= cols; i++) {
            if(i == col) continue;
            for(int j = 1; j <= rows; j++) {
                if(j == row) continue;
                T value = valueAt(i, j);
                compl.setvalueAt(i < col ? i : i-1, j < row ? j : j-1, value);
            }
        }

        return compl;
    }

    /**
     * Calculates the adjunct of an element of this matrix. The adjunct is
     * the complementary minor of this element and if {@code row + col} is odd,
     * the returned matrix is multiplied by -1
     * @param row row of the element
     * @param col column of the element
     * @return adjunct of the element
     */
    public Matrix<T> adjunct(int row, int col) {
        if((row + col) % 2 == 0) {
            return complementaryMinor(row, col);
        } else {
            return complementaryMinor(row, col).apply((v) ->
                ops.mul(v, ops.convert(-1.f))
            );
        }
    }

    /**
     * Applies a function on all values of the matrix
     * @param function function to apply
     * @return itself
     */
    public Matrix<T> apply(Function<T, T> function) {
        for(int row = 1; row <= rows; row++) {
            for(int col = 1; col <= cols; col++) {
                setvalueAt(col, row, function.apply(valueAt(col, row)));
            }
        }
        det = null;
        return this;
    }

    /**
     * @return true if matrix has the same number of columns and rows
     */
    public boolean isSquare() {
        return cols == rows;
    }

    /**
     * @param col number of a column
     * @return the column as a {@link Vector}
     * @throws IndexOutOfBoundsException if the column is not valid
     */
    public Vector<T> getColumn(int col) {
        Vector<T> column = new Vector<>(rows, ops);

        for(int row = 1; row <= rows; row++) {
            column.setCoord(row, getValueAt(row, col));
        }

        return column;
    }

    /**
     * @param row number of a row
     * @return the row as a {@link Vector}
     * @throws IndexOutOfBoundsException if the row is not valid
     */
    public Vector<T> getRow(int row) {
        Vector<T> column = new Vector<>(cols, ops);

        for(int col = 1; col <= cols; col++) {
            column.setCoord(col, getValueAt(row, col));
        }

        return column;
    }

    /**
     * Sets the column {@code col} with the values of the {@link Vector}
     * @param col number of a column
     * @param vector column vector to set
     * @return this matrix
     * @throws IndexOutOfBoundsException if the column is not valid
     * @throws IllegalArgumentException if the vector has a different number of
     *      coordinates than the number of rows of this matrix
     */
    public Matrix<T> setColumn(int col, Vector<T> vector) {
        if(vector.getNumCoords() != rows)
            throw new IllegalArgumentException("Vector has different number of" +
                " coordinates that number of rows in the matrix");

        for(int row = 1; row <= rows; row++) {
            setValueAt(row, col, vector.getCoord(row));
        }

        det = null;
        return this;
    }

    /**
     * Sets the row {@code row} with the values of the {@link Vector}
     * @param row number of a row
     * @param vector row vector to set
     * @return this matrix
     * @throws IndexOutOfBoundsException if the row is not valid
     * @throws IllegalArgumentException if the vector has a different number of
     *      coordinates than the number of columns of this matrix
     */
    public Matrix<T> setRow(int row, Vector<T> vector) {
        if(vector.getNumCoords() != rows)
            throw new IllegalArgumentException("Vector has different number of" +
                " coordinates that number of columns in the matrix");

        for(int col = 1; col <= rows; col++) {
            setValueAt(row, col, vector.getCoord(col));
        }

        det = null;
        return this;
    }

    /**
     * Clones the matrix
     * @return the cloned matrix
     */
    @Override
    @SuppressWarnings("unchecked")
    public Matrix<T> clone() {
        try {
            Matrix<T> newOne = (Matrix<T>) super.clone();
            newOne.storage = Arrays.copyOf(storage, storage.length);
            newOne.det = null;
            return newOne;
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks whether an object is floatEquals to this matrix. An object is floatEquals
     * to this matrix if and only if type's object is Matrix or a subclass of
     * it, the type of number are the same, and the contents of the matrix are
     * floatEquals
     * @param o an object to test equality
     * @return true if both objects are floatEquals, false in case of not
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof Matrix) {
            Matrix mat = (Matrix) o;
            if(getNumberClass(storage[0]).equals(getNumberClass(mat.storage[0])) && cols == mat.cols && rows == mat.rows) {
                for(int i = 0; i < storage.length; i++) {
                    if(!GLM.floatEquals(storage[i].doubleValue(), mat.storage[i].doubleValue(), 0.001f)) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Computes hash code of the matrix
     * @return matrix' hash code
     */
    @Override
    public int hashCode() {
        int hash = rows + cols * 29;
        for(T num : storage) {
            hash = 29 * (hash + num.hashCode());
        }
        return hash;
    }

    /**
     * @return a string representation of the matrix
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int row = 1; row <= rows; row++) {
            for(int col = 1; col <= cols; col++) {
                sb.append(String.format("%+.3f ", valueAt(col, row).doubleValue()));
            }

            if(row != rows)
                sb.replace(sb.length() - 1, sb.length(), "\n");
            else
                sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    static Class getNumberClass(Number num) {
        if(num instanceof Float) {
            return Float.class;
        } else if(num instanceof Double) {
            return Double.class;
        } else if(num instanceof Integer) {
            return Integer.class;
        } else if(num instanceof Long) {
            return Long.class;
        }
        return Number.class;
    }
}
