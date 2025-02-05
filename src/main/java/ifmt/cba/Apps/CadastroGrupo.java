package ifmt.cba.Apps;

import ifmt.cba.VO.GrupoProdutoVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;

public class CadastroGrupo {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            
            String nome = JOptionPane.showInputDialog("Digite o nome do grupo");
            

            if (nome == null || nome.trim().isEmpty()) {
                System.out.println("Operação cancelada: nome não pode ser vazio");
                return;
            }

            
            
            GrupoProdutoVO grupo = new GrupoProdutoVO();
            grupo.setNome(nome); 
            

            
            em.getTransaction().begin();
            em.persist(grupo); 
            em.getTransaction().commit();

            JOptionPane.showMessageDialog(null, "Grupo cadastrado com sucesso!");

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
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