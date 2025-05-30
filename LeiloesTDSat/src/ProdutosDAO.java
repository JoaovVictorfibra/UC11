/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */

import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;

public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void cadastrarProduto (ProdutosDTO produto){
        
        String sql = "INSERT INTO produtos (nome, valor, status) VALUES(?, ?, ?)";
        
        try{
            conn = new conectaDAO().connectDB();
            prep = conn.prepareStatement(sql);
            prep.setString(1, produto.getNome());
            prep.setInt(2, produto.getValor());
            prep.setString(3, produto.getStatus());
            
            prep.executeUpdate();
        }catch(SQLException e){
            System.out.println("Erro ao cadastrar porduto: " + e.getMessage());
            } 
    }
    
    public ArrayList<ProdutosDTO> listarProdutos(){
        conn = new conectaDAO().connectDB();
        
        try{
            String sql = "SELECT * FROM produtos";
            prep = conn.prepareStatement(sql);
            resultset = prep.executeQuery();
            
            while(resultset.next()){
                ProdutosDTO produtos = new ProdutosDTO();
                produtos.setId(resultset.getInt("id"));
                produtos.setNome(resultset.getString("nome"));
                produtos.setValor(resultset.getInt("valor"));
                produtos.setStatus(resultset.getString("status"));
                
                listagem.add(produtos);
            }
        }catch(SQLException e){
            System.out.println("Erro ao listar produtos" + e.getMessage());
        }finally{
            try{
                if(resultset != null) resultset.close();
                if(prep != null) prep.close();
                //if(conn != null) conn.close();
            }catch(SQLException ex){
                System.out.println("Erro ao fechar recursos: " + ex.getMessage());
            }
        }
        
        return listagem;
    }
    
    public void venderProduto(int idProduto){
        String sql = "UPDATE produtos SET status = ? WHERE id = ?";
        
        try{
            conn = new conectaDAO().connectDB();
            prep = conn.prepareCall(sql);
            prep.setString(1, "Vendido");
            prep.setInt(2, idProduto);
            
            int atualizado = prep.executeUpdate();
            
            if(atualizado > 0){
                System.out.println("Produto com ID " + idProduto + " foi marcado como 'Vendido'.");
            }else{
                System.out.println("Nenhum produto encontrado com o ID " + idProduto);
            }
        }catch(SQLException e){
            System.out.println("Erro ao vender produto: " + e.getMessage());
        }
    }
    
    public ArrayList<ProdutosDTO> listarProdutosVendidos(){
        ArrayList<ProdutosDTO> vendidos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";
        try{
            conn = new conectaDAO().connectDB();
            prep = conn.prepareStatement(sql);
            resultset = prep.executeQuery();
            
            while(resultset.next()){
                ProdutosDTO prod = new ProdutosDTO();
                prod.setId(resultset.getInt("id"));
                prod.setNome(resultset.getString("nome"));
                prod.setValor(resultset.getInt("valor"));
                prod.setStatus(resultset.getString("status"));
                
                vendidos.add(prod);
            }
        }catch(SQLException e){
            System.out.println("Erro ao listar produtos vendidos: " + e.getMessage());
        }
        return vendidos;
    }    
}

