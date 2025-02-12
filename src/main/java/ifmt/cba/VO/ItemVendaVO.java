package ifmt.cba.VO;

import jakarta.persistence.*;

@Entity
@Table(name = "item_venda")
public class ItemVendaVO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private int codigo;
    
    @Column(name = "quantidade", nullable = false)
    private int quantidade;
    
    @Column(name = "preco_venda", nullable = false)
    private float precoVenda;
    
    @Column(name = "per_desconto")
    private float perDesconto;
    
    @ManyToOne
    @JoinColumn(name = "produto_codigo", nullable = false)
    private ProdutoVO produto;
    
    @ManyToOne
    @JoinColumn(name = "venda_codigo", nullable = false)
    private VendaVO venda;

    // Getters e Setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(float precoVenda) {
        this.precoVenda = precoVenda;
    }

    public float getPerDesconto() {
        return perDesconto;
    }

    public void setPerDesconto(float perDesconto) {
        this.perDesconto = perDesconto;
    }

    public ProdutoVO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoVO produto) {
        this.produto = produto;
    }

    public VendaVO getVenda() {
        return venda;
    }

    public void setVenda(VendaVO venda) {
        this.venda = venda;
    }

    public float getValorTotal() {
        float valorSemDesconto = this.quantidade * this.precoVenda;
        return valorSemDesconto - (valorSemDesconto * (this.perDesconto / 100));
    }
}