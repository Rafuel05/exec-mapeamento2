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

            GrupoProdutoVO grupo = new GrupoProdutoVO();
            String nome = JOptionPane.showInputDialog("Digite o nome do grupo");
            int codigo = Integer.parseInt(JOptionPane.showInputDialog("Digite o id do grupo"));

            EntityManagerFactory factory = Persistence.createEntityManagerFactory("Testando");

            em.getTransaction().begin();
            em.merge(grupo);
            em.getTransaction().commit();
            em.close();


        }catch (Exception ex) {
            System.out.println("Inclusão não realizada: " + ex.getMessage());
            ex.printStackTrace();

        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }
}