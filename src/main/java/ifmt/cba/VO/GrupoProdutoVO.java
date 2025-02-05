package ifmt.cba.VO;
import jakarta.persistence.*;

@Entity
@Table(name = "grupo_produto")
public class GrupoProdutoVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo; // Mudado para Long para melhor compatibilidade com PostgreSQL

    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    // Getters e Setters
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}