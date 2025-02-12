package ifmt.cba.VO;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

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

    @ManyToOne
    @JoinColumn(name = "grupo_codigo", referencedColumnName = "codigo")
    private GrupoProdutoVO grupo;

    @ManyToMany(mappedBy = "produtos")
    private List<FornecedorVO> fornecedores = new ArrayList<>();

    // Getters e Setters existentes
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

    // Novos getters e setters para fornecedores
    public List<FornecedorVO> getFornecedores() {
        return fornecedores;
    }

    public void setFornecedores(List<FornecedorVO> fornecedores) {
        this.fornecedores = fornecedores;
    }

    public void addFornecedor(FornecedorVO fornecedor) {
        if (fornecedor != null && !this.fornecedores.contains(fornecedor)) {
            this.fornecedores.add(fornecedor);
            fornecedor.addProduto(this);
        }
    }

    public void removeFornecedor(FornecedorVO fornecedor) {
        if (this.fornecedores.remove(fornecedor)) {
            fornecedor.removeProduto(this);
        }
    }
}