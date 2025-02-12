package ifmt.cba.VO;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name="pessoa")
@Inheritance(strategy = InheritanceType.JOINED) // Mudando a estratégia para JOINED
public abstract class PessoaVO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) // Mudando para SEQUENCE
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "nome")
    private String nome;

    // Getters e Setters permanecem os mesmos
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