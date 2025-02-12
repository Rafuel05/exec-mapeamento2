package ifmt.cba.Apps;

import ifmt.cba.VO.FornecedorVO;
import ifmt.cba.VO.ProdutoVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import javax.swing.*;
import java.util.List;

public class CadastroFornecedor {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Coleta dados básicos
            String nomeFantasia = JOptionPane.showInputDialog("Digite o nome fantasia do fornecedor");
            if (nomeFantasia == null || nomeFantasia.trim().isEmpty()) {
                System.out.println("Operação cancelada: nome fantasia não pode ser vazio");
                return;
            }

            String razaoSocial = JOptionPane.showInputDialog("Digite a razão social do fornecedor");
            if (razaoSocial == null || razaoSocial.trim().isEmpty()) {
                System.out.println("Operação cancelada: razão social não pode ser vazia");
                return;
            }

            // Cria e popula o FornecedorVO
            FornecedorVO fornecedor = new FornecedorVO();
            fornecedor.setNomeFantasia(nomeFantasia);
            fornecedor.setRazaoSocial(razaoSocial);

            // Busca produtos disponíveis
            TypedQuery<ProdutoVO> query = em.createQuery("SELECT p FROM ProdutoVO p", ProdutoVO.class);
            List<ProdutoVO> produtos = query.getResultList();

            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não existem produtos cadastrados. Cadastre produtos primeiro.");
                return;
            }

            // Seleciona produtos para o fornecedor
            while (true) {
                StringBuilder produtosStr = new StringBuilder("Selecione o código do produto um de cada vez (ou cancele para finalizar/cadastrar):\n");
                for (ProdutoVO produto : produtos) {
                    produtosStr.append(String.format("Código: %d - %s (R$ %.2f)\n", 
                        produto.getCodigo(), produto.getNome(), produto.getPrecoVenda()));
                }

                String codigoStr = JOptionPane.showInputDialog(produtosStr.toString());
                if (codigoStr == null || codigoStr.trim().isEmpty()) {
                    break;
                }

                try {
                    Long codigo = Long.parseLong(codigoStr);
                    ProdutoVO produto = em.find(ProdutoVO.class, codigo);
                    if (produto != null) {
                        fornecedor.addProduto(produto);
                    } else {
                        JOptionPane.showMessageDialog(null, "Produto não encontrado!");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Código inválido!");
                }
            }

            if (fornecedor.getProdutos().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O fornecedor precisa ter pelo menos um produto!");
                return;
            }

            // Persiste o fornecedor
            em.getTransaction().begin();
            em.persist(fornecedor);
            em.getTransaction().commit();

            // Monta mensagem de sucesso
            StringBuilder mensagem = new StringBuilder();
            mensagem.append("Fornecedor cadastrado com sucesso!\n");
            mensagem.append("Código: ").append(fornecedor.getCodigo()).append("\n");
            mensagem.append("Nome Fantasia: ").append(fornecedor.getNomeFantasia()).append("\n");
            mensagem.append("Razão Social: ").append(fornecedor.getRazaoSocial()).append("\n");
            mensagem.append("\nProdutos vinculados:\n");
            for (ProdutoVO produto : fornecedor.getProdutos()) {
                mensagem.append(String.format("- %s (R$ %.2f)\n", produto.getNome(), produto.getPrecoVenda()));
            }

            JOptionPane.showMessageDialog(null, mensagem.toString());

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar fornecedor: " + ex.getMessage());
            System.out.println("Inclusão não realizada: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
    }
}