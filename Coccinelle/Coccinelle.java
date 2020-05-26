// programme pour la partie 1

import java.util.Scanner;
import java.lang.Math;



public class Coccinelle {


	// Méthode principale qui se contente d'appeler d'autres fonctions
	public static void main (String[] args){
		
		int[][] grille = {
			{2,4,3,9,6},
			{1,10,15,1,2},
			{2,4,11,26,66},
			{36,34,1,13,30},
			{46,2,8,7,15},
			{89,27,1,12,3},
			{1,72,3,6,6},
			{3,1,2,4,5}};
		System.out.println("La grille de base est : ");
		printGrille(grille);

		System.out.println("La matrice M[L][C] de terme général m(l,c) :");
		int M[][] = calculGrille(grille);
		printGrille(M);
		
		System.out.println("Résultats et analyse : ");
		int total = getTotal(M);
		System.out.println("La coccinelle a mangé : "+total+" pucerons.");

		int[][] path = getPath(M);
		System.out.print("Ella a parcouru le chemin : ");
	       	printPath(path);

		int[] firstCase = getFirstCase(path);
		System.out.print("Elle a atteri sur la case : "); 
		printCase(firstCase);
		
		int[] lastCase = getLastCase(path);
		System.out.print("Elle a fait l'interview sur la case : "); 
		printCase(lastCase);
		
		System.exit(0);
	}
	

	//calcul le tableau M[c][l] de termes général m(c,l)
	public static int[][] calculGrille(final int[][] grille){

		int[][] M = new int[8][5];	
	
		//cas initial
		for(int c=0; c < 5; c++){M[0][c]=grille[0][c];}

		//cas général
		for (int l = 1 ; l < M.length ; l++){		
			for(int c = 0 ; c < M[0].length; c++){

				if(c == M[0].length-1) M[l][c] = grille[l][c] + max2(M[l-1][c],M[l-1][c-1]);

				else if (c == 0) M[l][c]=grille[l][c]+max2(M[l-1][c],M[l-1][c+1]);

				else M[l][c] = grille[l][c] + max3(M[l-1][c-1],M[l-1][c],M[l-1][c+1]);
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
		System.out.println("");
	}

	
	public static int max3(final int n1, final int n2, final int n3){
		return Math.max(n1,Math.max(n2,n3));
	}

	public static int max2(final int n1, final int n2){
		return Math.max(n1,n2);
	}

	public static int[] maxLigne(final int[] ligne){
		
		int[] retour = new int[2];
		int maxLigne = 0, positionLigne = 0;

		for(int i=0; i<ligne.length; i++){
			if(ligne[i] > maxLigne){
				maxLigne = ligne[i];
				positionLigne = i;
			}
		}
		retour[0]=maxLigne;
		retour[1]=positionLigne;
		return retour;
		
	}

	public static int getTotal(final int[][] M){
		return maxLigne(M[M.length-1])[0];
	}

	public static int[][] getPath( final int[][] M){
		int[][] path = new int[M.length][2];
		
		

		return path;

	}



	public static int[] getFirstCase(final int[][] path){
		return path[0];
	}

	public static void printCase(final int[] cellule){
		System.out.println("("+cellule[0]+","+cellule[1]+").");
	}


	public static int[] getLastCase(final int[][] path){
		return path[path.length-1];
	}
	
	public static void printPath(int[][] path){
		for(int i = 0; i < path.length; i++){
			System.out.print("("+path[i][0]+","+path[i][1]+")");
		}
		System.out.println("");
	}
}
