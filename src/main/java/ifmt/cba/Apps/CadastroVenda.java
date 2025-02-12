package ifmt.cba.Apps;

import ifmt.cba.VO.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import javax.swing.*;
import java.util.Date;
import java.util.List;

public class CadastroVenda {
    
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Buscar clientes disponíveis
            TypedQuery<ClienteVO> queryClientes = em.createQuery("SELECT c FROM ClienteVO c", ClienteVO.class);
            List<ClienteVO> clientes = queryClientes.getResultList();

            if (clientes.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não existem clientes cadastrados!");
                return;
            }

            // Selecionar cliente
            StringBuilder clientesStr = new StringBuilder("Selecione o código do cliente:\n");
            for (ClienteVO cliente : clientes) {
                clientesStr.append(String.format("Código: %d - %s (CPF: %s)\n", 
                    cliente.getCodigo(), cliente.getNome(), cliente.getCpf()));
            }

            String codigoClienteStr = JOptionPane.showInputDialog(clientesStr.toString());
            if (codigoClienteStr == null || codigoClienteStr.trim().isEmpty()) {
                return;
            }

            ClienteVO cliente = em.find(ClienteVO.class, Long.parseLong(codigoClienteStr));
            if (cliente == null) {
                JOptionPane.showMessageDialog(null, "Cliente não encontrado!");
                return;
            }

            // Buscar vendedores disponíveis
            TypedQuery<VendedorVO> queryVendedores = em.createQuery("SELECT v FROM VendedorVO v", VendedorVO.class);
            List<VendedorVO> vendedores = queryVendedores.getResultList();

            if (vendedores.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não existem vendedores cadastrados!");
                return;
            }

            // Selecionar vendedor
            StringBuilder vendedoresStr = new StringBuilder("Selecione o código do vendedor:\n");
            for (VendedorVO vendedor : vendedores) {
                vendedoresStr.append(String.format("Código: %d - %s (Comissão: %.2f%%)\n", 
                    vendedor.getCodigo(), vendedor.getNome(), vendedor.getPerComissao()));
            }

            String codigoVendedorStr = JOptionPane.showInputDialog(vendedoresStr.toString());
            if (codigoVendedorStr == null || codigoVendedorStr.trim().isEmpty()) {
                return;
            }

            VendedorVO vendedor = em.find(VendedorVO.class, Long.parseLong(codigoVendedorStr));
            if (vendedor == null) {
                JOptionPane.showMessageDialog(null, "Vendedor não encontrado!");
                return;
            }

            // Criar venda
            VendaVO venda = new VendaVO();
            venda.setDataVenda(new Date());
            venda.setCliente(cliente);
            venda.setVendedor(vendedor);

            // Buscar produtos disponíveis
            TypedQuery<ProdutoVO> queryProdutos = em.createQuery("SELECT p FROM ProdutoVO p", ProdutoVO.class);
            List<ProdutoVO> produtos = queryProdutos.getResultList();

            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não existem produtos cadastrados!");
                return;
            }

            // Loop para adicionar itens
            boolean continuarAdicionando = true;
            while (continuarAdicionando) {
                // Selecionar produto
                StringBuilder produtosStr = new StringBuilder("Selecione o código do produto:\n");
                for (ProdutoVO produto : produtos) {
                    produtosStr.append(String.format("Código: %d - %s (Preço: R$ %.2f)\n", 
                        produto.getCodigo(), produto.getNome(), produto.getPrecoVenda()));
                }

                String codigoProdutoStr = JOptionPane.showInputDialog(produtosStr.toString());
                if (codigoProdutoStr == null || codigoProdutoStr.trim().isEmpty()) {
                    if (venda.getItens().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "A venda precisa ter pelo menos um item!");
                        continue;
                    }
                    break;
                }

                ProdutoVO produto = em.find(ProdutoVO.class, Long.parseLong(codigoProdutoStr));
                if (produto == null) {
                    JOptionPane.showMessageDialog(null, "Produto não encontrado!");
                    continue;
                }

                // Criar item venda
                ItemVendaVO item = new ItemVendaVO();
                item.setProduto(produto);
                item.setPrecoVenda(produto.getPrecoVenda());

                // Quantidade
                String quantidadeStr = JOptionPane.showInputDialog("Digite a quantidade:");
                if (quantidadeStr == null || quantidadeStr.trim().isEmpty()) {
                    continue;
                }
                item.setQuantidade(Integer.parseInt(quantidadeStr));

                // Desconto
                String descontoStr = JOptionPane.showInputDialog("Digite o percentual de desconto (0-100):");
                if (descontoStr != null && !descontoStr.trim().isEmpty()) {
                    float desconto = Float.parseFloat(descontoStr);
                    if (desconto >= 0 && desconto <= 100) {
                        item.setPerDesconto(desconto);
                    }
                }

                venda.addItem(item);

                int opcao = JOptionPane.showConfirmDialog(null, 
                    "Deseja adicionar mais produtos?", 
                    "Continuar", 
                    JOptionPane.YES_NO_OPTION);
                continuarAdicionando = (opcao == JOptionPane.YES_OPTION);
            }

            // Persistir venda
            em.getTransaction().begin();
            em.persist(venda);
            em.getTransaction().commit();

            // Mostrar resumo da venda
            StringBuilder resumo = new StringBuilder();
            resumo.append("Venda realizada com sucesso!\n\n");
            resumo.append(String.format("Código da Venda: %d\n", venda.getCodigo()));
            resumo.append(String.format("Data: %tF %tT\n", venda.getDataVenda(), venda.getDataVenda()));
            resumo.append(String.format("Cliente: %s\n", venda.getCliente().getNome()));
            resumo.append(String.format("Vendedor: %s\n\n", venda.getVendedor().getNome()));
            resumo.append("Itens da Venda:\n");
            
            float valorTotal = 0;
            for (ItemVendaVO item : venda.getItens()) {
                resumo.append(String.format("- %s (x%d) - R$ %.2f (Desc: %.2f%%) = R$ %.2f\n",
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.getPrecoVenda(),
                    item.getPerDesconto(),
                    item.getValorTotal()));
                valorTotal += item.getValorTotal();
            }
            
            resumo.append(String.format("\nValor Total da Venda: R$ %.2f", valorTotal));
            
            JOptionPane.showMessageDialog(null, resumo.toString());

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar venda: " + ex.getMessage());
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