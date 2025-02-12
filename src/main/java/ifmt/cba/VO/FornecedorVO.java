package ifmt.cba.VO;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "fornecedor")
public class FornecedorVO extends PessoaJuridicaVO {

    @ManyToMany
    @JoinTable(
        name = "fornecedor_produto",
        joinColumns = @JoinColumn(name = "fornecedor_codigo"),
        inverseJoinColumns = @JoinColumn(name = "produto_codigo")
    )
    private List<ProdutoVO> produtos = new ArrayList<>();

    public List<ProdutoVO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoVO> produtos) {
        this.produtos = produtos;
    }

    public void addProduto(ProdutoVO produto) {
        if (produto != null && !this.produtos.contains(produto)) {
            this.produtos.add(produto);
        }
    }

    public void removeProduto(ProdutoVO produto) {
        this.produtos.remove(produto);
    }
}