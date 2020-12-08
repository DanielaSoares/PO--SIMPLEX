/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;


/*
Importando o simplex
*/
import entidade.Simplex;

/**
 *
 * @author daniela
 */
public class Inicio {
    public static void main(String[] args) {
        
        /*FO: MAX Z = 100x1+150x2
        SA:
            2X1+3X2<=120
            X1<=40
            X2<=30
            x1, x2>=0
        
        FORMA PADRAO SIMPLEX
        
        z-100x1-150x2+xf1+xf2+xf3=0
        2x1+3x2+xf1=120
        x1+xf2=40
        x2+xf3=30
        
        */
        Simplex simplex = new Simplex(2, 3); //cria uma variável do tipo Simplex e ela recebe um novo simplex de 2 linhas e 3 colunas
        double[] fo = {1,-100,-150,0,0,0,0}; // varíavel fo recebe os valores da função objetivo 
        
        simplex.setFo(fo); //determina os valores da função objetivo
        
        double[] sa1 = {0,2,3,1,0,0,120}; //variavel sa1 recebe os valores da primeira restrição
        simplex.adiciona_restricao(sa1); //Adiciona a primeira restrição
        
        double[] sa2 = {0,1,0,0,1,0,40}; //variável sa2 recebe os valores da segunda restrição
        simplex.adiciona_restricao(sa2); // Adiciona a segunda restrição
        
        double[] sa3 = {0,0,1,0,0,1,30}; //variavel sa3 recebe os valores da terceira restriçaõ
        simplex.adiciona_restricao(sa3); //adiciona a terceira restrição
        
        
        simplex.resolver(); 
                
    }
}
