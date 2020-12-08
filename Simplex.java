/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author daniela
 */
public class Simplex {

    private double[][] tabela; 
    private double[] fo;
    private int indice;

    public Simplex(int qtd_var, int qtd_sa) { //define um construtor que inicializa a tabela vazia
        this.tabela = new double[qtd_sa + 1][qtd_var + qtd_var]; //Simplex trabalhará em cima dessa tabela
        this.indice = 0; //faz uma serie de procedimentos e calculos para se obter novas linhas e o resultado final.
    }

    public void setFo(double[] fo) { //função para definir a função objetivo que recebe como parametro uma lista(indice)
        this.fo = fo; //vai receber uma lista com os valores da função objetivo e atribuir a primeira linha a tabela.  sempre a primeira linha vai ser a da funçao objetivo.
        this.tabela[this.indice] = this.fo;
        this.indice++;
    }

    public void adiciona_restricao(double[] sa) { //funçao para adicionar as restriçoes
        this.tabela[this.indice] = sa; //vai receber uma lista que vai ser a lista de restrições
        this.indice++; //faz o mesmo que o anterior porem adicionando restrições em cada linha da tabela\aAAAAAAA\
    }

    public void exibir_tabela() { //Imprime  a tabela 
        get_coluna_entra(); 
        for (int i = 0; i < tabela.length; i++) {
            for (int j = 0; j < tabela[i].length; j++) {
                System.out.print(tabela[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    private int get_coluna_entra() { //vai retornar um inteira que é responsavel por representar o pivo das linhas
        double[] linha = this.tabela[0];
        double menor = Integer.MAX_VALUE; // o pivo vai ser o menor valor negativo da lista. Vai retornar o menor valor da linha 0, que no caso é a linha da função objetivo.
        int indice = 0;
        for (int i = 0; i < linha.length; i++) { //identifica a coluna que está entrando, que no caso é a que contem o pivo, o numero de menor valor negativo
            if (menor > linha[i]) { 
                menor = linha[i];
                indice = i;
            }
        }

        return indice; 
    }
    //execto a linha da função objeitivo para se localiizar as outras linhas que vão sair é feito uma divisão da ultima coluna que é da de resultados pelo pivo da propria linha. o menor valor da divisão vai representar a linha que está saindo.
    public int get_linha_que_sai() { //função que localiza a linha que vai sair. retorna a linha do indice que sai
        int coluna_pivo = get_coluna_entra();  //recebe a coluna que entra

        Map<Integer, Double> map = new HashMap<>();

        for (int i = 0; i < tabela.length; i++) { //vai percorrer todas as linhas da tabela
            if (i > 0) {
                if (tabela[i][coluna_pivo] > 0) { //se nao for a linha objetivo vai fazer o calculo para ver qul vai sair
                    double valor = tabela[i][tabela[i].length-1] / tabela[i][coluna_pivo]; 
                    map.put(i, valor); //armazena o resultado da divisão das linhas
                }
            }
        }

        Double min = Collections.min(map.values()); //Acha O Valor Min baseado no valor armazenado na chave
        
        Object chave = getIndiceLinha(map, min); 

        return (int) chave; 
    }

    private Object getIndiceLinha(Map hm, Object value) { 
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
    
    private double[] calcular_linha_pivo(int coluna_pivo, int linha_sai){ 
        double[] linha = tabela[linha_sai]; 
        double[] nova_linha_pivo = new double[linha.length];
        double pivo = linha[coluna_pivo];
        
        for(int i = 0; i < linha.length; i++){
            nova_linha_pivo[i] = linha[i] / pivo;
        }
        
        return nova_linha_pivo;
    }
    
    private double[] calcular_nova_linha(double[] linha, int coluna_pivo, double[] linha_pivo){ //função para calcular nova linha
        double pivo = linha[coluna_pivo] * (-1); //acha o pivo da linha que vai ser substituida. faz a multiplicação por -1 que é regra do algoritmo do simplex
        double[] result_linha = new double[linha.length]; //valor do pivô multiplicado por cada elemento da linha e somado a linha pivo. Vai gerar uma nova linha 
        
        for(int i = 0; i < linha_pivo.length; i++){ 
            result_linha[i] = linha_pivo[i] * pivo; //percorre a linha e faz a multiplicação. Resulta em uma linha depois de feito os calculos
        }
        
        double[] nova_linha = new double[linha.length]; //declara uma nova linha vazia
        for(int i = 0; i < result_linha.length; i++){ 
            nova_linha[i] = result_linha[i] + linha[i]; 
        }
        
        return nova_linha;
    }
    
    private double[][] copiar_tabela(){ //É criado uma cópia da tabela sem que haja alteração da original
        double[][] copia = new double[tabela.length][tabela[0].length];
        for(int i = 0; i < tabela.length; i++){
            for(int j = 0; j < tabela[0].length; j++){
                copia[i][j] = tabela[i][j];
            }
        }
        return copia;
    }
    
    private boolean verifica_negativo(){ //enquanto a primeira linha obter numeros negativos a tabela deve ser recalculada
        for(int i = 0; i < tabela[0].length; i++){
            if(tabela[0][i] < 0.0){ 
                return true;
            }
        }
        return false;
    }
    
    private void processar(){ //função para recalcular a tabela
        int coluna_pivo = get_coluna_entra(); //chama a função get_coluna_entra pra identificar a coluna que entra
        int linha_sai = get_linha_que_sai(); // chama a função get_linha_sai para identificar a linha que sai
        
        double[] linha_pivo = calcular_linha_pivo(coluna_pivo, linha_sai); //calcular a linha pivo que retorna o indice da linha pivo
        double[][] copia = copiar_tabela(); //recebe a tabela que foi copiada anteriormente e faz uma copia substituindo os valores
        
        tabela[linha_sai] = linha_pivo;
        
        int i = 0;
        while(i < tabela.length){ //enquanto i for < que o tamanho da tabela original faz a exexução do recalculo das novas linhas.
            if(i != linha_sai){ 
                double[] linha_atual = copia[i];
                double[] nova_linha = calcular_nova_linha(linha_atual, coluna_pivo, linha_pivo);
                tabela[i] = nova_linha; //substituição na tabela original
            }
            i++;
        }
    }
    
    public void resolver(){//faz uso do metodo negativo, equanto houver numero negativo vai chamar o metodo para calcular a tabela de novo até que se encontre uma solução otima
        processar();
        
        while(verifica_negativo()){ //enquanto tiver numero negativo vai recalcular a tabela
            processar(); //executa o algoritmo
        }
        
        exibir_tabela();// mostra a  nova tabela criada
    }
}
