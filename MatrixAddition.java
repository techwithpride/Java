public class MatrixAddition{
    public static void main(String args[]){
        if(args.length!=1){
            System.out.println("Usage:Java Matrix Addition <order-of-matrix>.");
            return;
        }
        int N=Integer.parseInt(args[0]);
        if(N<=0){
            System.out.println("order should be a positive integer.");
            return;
        }
        int[][] matrix1=new int[N][N];
        int[][] matrix2=new int[N][N];

        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                matrix1[i][j]=i+j;
                matrix2[i][j]=i-j;
            }
        }
        System.out.println("Matrix 1:");
        displayMatrix(matrix1);

        System.out.println("Matrix 2:");
        displayMatrix(matrix2);

        int[][] sumMatrix=addMatrices(matrix1,matrix2);
        System.out.println("Sum of matrices: ");
        displayMatrix(sumMatrix);
    }
    public static int[][] addMatrices(int[][] matrix1,int[][] matrix2){
        int N=matrix1.length;
        int[][] sumMatrix=new int[N][N];
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                sumMatrix[i][j]=matrix1[i][j]+matrix2[i][j];
            }
        }
        return sumMatrix;
    }
    private static void displayMatrix(int[][] matrix){
        int N=matrix.length;
        for(int i=0;i<N;i++){
            for(int value:matrix[i]){
                System.out.print(value+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
}