package ifmt.cba.VO;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "venda")
public class VendaVO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private int codigo;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_venda", nullable = false)
    private Date dataVenda;
    
    @ManyToOne
    @JoinColumn(name = "cliente_codigo", nullable = false)
    private ClienteVO cliente;
    
    @ManyToOne
    @JoinColumn(name = "vendedor_codigo", nullable = false)
    private VendedorVO vendedor;
    
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ItemVendaVO> itens = new ArrayList<>();

    // Getters e Setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public ClienteVO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteVO cliente) {
        this.cliente = cliente;
    }

    public VendedorVO getVendedor() {
        return vendedor;
    }

    public void setVendedor(VendedorVO vendedor) {
        this.vendedor = vendedor;
    }

    public List<ItemVendaVO> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaVO> itens) {
        this.itens = itens;
    }

    public void addItem(ItemVendaVO item) {
        item.setVenda(this);
        this.itens.add(item);
    }
}