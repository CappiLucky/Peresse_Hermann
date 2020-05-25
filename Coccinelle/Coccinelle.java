// programme pour la partie 1

import java.util.Scanner;



public class Coccinelle {


	// Méthode principale qui se contente d'appeler d'autres fonctions
	public static void main (String[] args){
		int[][] grille = {{2,4,3,9,6},{1,10,15,1,2},{2,4,11,26,66},{36,34,1,13,30},{46,2,8,7,15},{89,27,10,12,3},{1,72,3,6,6},{3,1,2,4,5}};
		printGrille(grille);
		int M[][] = calculGrille(grille);
		System.exit(1);
	}
	
	//calcul le tableau M[c][l] de termes général m(c,l)
	public static int[][] calculGrille(final int[][] grille){
		int[][] M = new int[5][8];	
		//cas initial
		for(int c=0; c < 5; c++){M[0][c]=grille[0][c];}

		//cas général
		for (int l = 7 ; l > 0 ; l++){
			
			for(int c = 7; c > 0; c++){
				M[l][c] = grille[l][c] + max3(M[l-1][c],M[l-1][c-1],M[l-1][c+1]);
			}
		}
		return M;


	}
	
	public static void printGrille(final int[][] grille){
		for(int i=0;i < grille.length; i++){
			for(int j=0;j<grille[0].length;j++){
				System.out.print(grille[i][j] + " ");
			}
			System.out.println("");
		}
	}

	
	public static int max3(final int n1, final int n2, final int n3){
		if( n1 > n2 && n1 > n3) return n1;
		if( n2 > n1 && n2 > n3) return n2;
		if( n3 > n1 && n3 > n2) return n3;
		return 0;
	}







}
