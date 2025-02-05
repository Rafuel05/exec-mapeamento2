package ifmt.cba.VO;

import jakarta.persistence.*;

@Entity
@Table(name = "produto")
public class ProdutoVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @Column(name = "preco_venda", nullable = false)
    private Float precoVenda;

    // Se precisar da relação com GrupoProduto
    @ManyToOne
    @JoinColumn(name = "grupo_codigo", referencedColumnName = "codigo")
    private GrupoProdutoVO grupo;

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

    public Float getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(Float precoVenda) {
        this.precoVenda = precoVenda;
    }

    public GrupoProdutoVO getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoProdutoVO grupo) {
        this.grupo = grupo;
    }
}