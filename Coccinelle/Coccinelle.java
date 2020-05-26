// programme pour la partie 1

import java.lang.Math; //permet d'importer la class Math pour calculer les max

/**
 *@author Manon HERMANN Théo PERESSE
 *@version V1
 *Ce programme est un programme de programmation dynamique afin de trouver le chemin le plus
 *rentable en mangant le plus de pucerons possible.
 */



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


	/**
	 * Calcul le tableau M[C][L] de terme général m(c,l)
	 * @param grille Une grille qui sert de base pour le calcul
	 * @return M la matrice M
	 */

	public static int[][] calculGrille(final int[][] grille){

		int[][] M = new int[grille.length][grille[0].length];

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

	/**
	 * Permet d'afficher une grille simplement
	 * @param grille La grille a afficher
	 */
	public static void printGrille(final int[][] grille){
		for(int i=grille.length-1;i >= 0; i--){
			for(int j=0;j<grille[0].length;j++){
				System.out.print(grille[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println("");
	}


	/**
	 * Retourne la valeur max entre les 3 parametres
	 * @param n1 valeur1
	 * @param n2 valeur2
	 * @param n3 valeur3
	 * @return max Le max entre les 3
	 * @see max2 pour le max entre 2 valeurs
	 */
	public static int max3(final int n1, final int n2, final int n3){
		return Math.max(n1,Math.max(n2,n3));
	}


	/**
	 * Retourne la valeur max entre les 2 parametres
	 * @param n1 valeur1
	 * @param n2 valeur2
	 * @return max Le max entre les 2 valeurs
	 * @see max3 pour le max entre 3 valeurs
	 */
	public static int max2(final int n1, final int n2){
		return Math.max(n1,n2);
	}

	/**
	 * Donne un tableau de deux elements, donc le premier element est la valeur max présente sur la ligne, et le second sa position (indice)
	 * @param ligne un tableau donc on veut faire l'analyse
	 * @return tab tableau dont la structure est expliquée ci dessus
	 */
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

	/**
	 * Donne le total de pucerons mangé au final par la coccinelle si elle emprunte le meilleur chemin
	 * @param M la matrice dynamique
	 * @return max Le nombre max de puceron mangé
	 */
	public static int getTotal(final int[][] M){
		return maxLigne(M[M.length-1])[0];
	}

	/**
	 * Renvoie le chemin parcouru par la coccinelle sous la forme d'un tableau 2d
	 * Chaque ligne correspond a un déplacement
	 * Chaque element du tableau correspond a des coordonnées dans la grille (2 : une en x et une en y)
	 *@param M La matrice dynamique M
	 *@return path Le meilleur chemin a suivre
	 */
	public static int[][] getPath( final int[][] M){
		int[][] path = new int[M.length][2];

		path[M.length-1][0] = maxLigne(M[M.length-1])[1];
		path[M.length-1][1] = M.length-1;

		for (int y = M.length-1 ; y > 0 ; y--){
			path[y-1][1] = y-1; //on met a jour le y
			int x_actual = path[y][0];

			if (x_actual == 0){//cas ou on est tout a gauche
				if(M[y-1][x_actual] >= M[y-1][x_actual+1]){
					path[y-1][0] = x_actual;
				} else {
					path[y-1][0] = x_actual+1;
				}
			}

			else if (x_actual == M[0].length -1){ //cas ou on est tout a droite
				if(M[y-1][x_actual] >= M[y-1][x_actual-1]){
					path[y-1][0] = x_actual;
				} else path[y-1][0] = x_actual-1;
			}


			else { //cas général
				if (M[y-1][x_actual] >= M[y-1][x_actual+1] && M[y-1][x_actual] >= M[y-1][x_actual-1]) {
					path[y-1][0] = x_actual;
				}
				else if (M[y-1][x_actual+1] >= M[y-1][x_actual] && M[y-1][x_actual+1] >= M[y-1][x_actual-1]){
					path[y-1][0] = x_actual+1;
				}
				else {
					path[y-1][0] = x_actual-1;
				}
			}
		}

		return path;
	}



	/**
	 * Renvoie la premiere case, celle sur laquelle la coccinelle s'est posée a l'origine
	 * @param path Le chemin qu'a suivi la coccinelle
	 * @return case d'origine
	 */
	public static int[] getFirstCase(final int[][] path){
		return path[0];
	}

	/**
	 * Permet d'afficher la case passée en parametres
	 * @param la case a afficher
	 */
	public static void printCase(final int[] cellule){
		System.out.println("("+cellule[0]+","+cellule[1]+").");
	}

	/**
	 * Renvoie la dernière case, celle sur laquelle la coccinelle a fini son periple
	 * @param path le chemin optimal suivi par la coccinelle
	 * @return case de fin
	 */
	public static int[] getLastCase(final int[][] path){
		return path[path.length-1];
	}

	/**
	 * Permet d'afficher le chemin suivi par la coccinelle
	 * @param path le chemin suivi par la coccinelle
	 */
	public static void printPath(int[][] path){
		for(int i = 0; i < path.length; i++){
			System.out.print("("+path[i][0]+","+path[i][1]+")");
		}
		System.out.println("");
	}
}
