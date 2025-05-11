package com.luciano;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.luciano.model.DAO;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Tela extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCaminho;
	
	DAO dao = new DAO();
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tela frame = new Tela();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Tela() {
		setTitle("Cadastro via Excel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 614, 295);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Selecione o arquivo:");
		lblNewLabel.setBounds(51, 109, 107, 19);
		contentPane.add(lblNewLabel);
		
		txtCaminho = new JTextField();
		txtCaminho.setBounds(176, 109, 249, 19);
		contentPane.add(txtCaminho);
		txtCaminho.setColumns(10);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					enviarCSV();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		btnEnviar.setBounds(232, 168, 96, 25);
		contentPane.add(btnEnviar);
		
		JButton btnProcurar = new JButton("Procurar");
		btnProcurar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buscarCaminho();
			}
		});
		btnProcurar.setBounds(460, 109, 96, 25);
		contentPane.add(btnProcurar);
	}
	
	private String buscarCaminho() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Selecionar arquivo");
		jfc.setFileFilter(new FileNameExtensionFilter("Arquivo de documentos(*.CSV)", "csv"));
		
		int resultado = jfc.showOpenDialog(null);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			if (file != null) {
				String caminho = file.getAbsolutePath();
				if (txtCaminho != null) {
					txtCaminho.setText(caminho);
				}
				return caminho;
			}
		}
		JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado!");
		return null;
	}
	
	private void enviarCSV() throws SQLException {
		String sql;
		int codigo;
		String nome, marca;
		BigDecimal preco;
		
		try {
			con = dao.conectar();
			String c = txtCaminho.getText();
			if (c == null) {
				JOptionPane.showMessageDialog(null, "Operação cancelada ou caminho inválido!");
				return;
			}
			try(FileReader arquivo = new FileReader(c);
					BufferedReader br = new BufferedReader(arquivo)){
				
				String linha;
				while ((linha = br.readLine()) !=  null) {
					String[] dados = linha.split(",");
					if (dados.length >= 4) {
						String codString = dados[0].replaceAll("[^0-9]", "");
						codigo = Integer.parseInt(codString);
						nome = dados[1];
						marca = dados[2];
						String precoString = dados[3].replaceAll("[\"\\s]", "");
						preco = new BigDecimal(precoString);
						
						sql = "select * from produto where codigo = ?";
						pst = con.prepareStatement(sql);
						pst.setInt(1, codigo);
						rs = pst.executeQuery();
						if (!rs.next()) {
							sql = "insert into produto(codigo, nome, marca, preco) values(?,?,?,?)";
							pst = con.prepareStatement(sql);
							pst.setInt(1, codigo);
							pst.setString(2, nome);
							pst.setString(3, marca);
							pst.setBigDecimal(4, preco);
							
							pst.executeUpdate();
							JOptionPane.showMessageDialog(null, "Produto de código " + codigo + " cadastrado com sucesso!");
						} else {
							JOptionPane.showMessageDialog(null, "Este produto de código " + codigo + " já está cadastrado!");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Formato inválido na linha: " + linha);
					}
					txtCaminho.setText(null);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao ler arquivo: " + e.getMessage());
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (pst!= null) {
					pst.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
}


