package ifmt.cba.VO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendedor")
public class VendedorVO extends PessoaFisicaVO {

    @Column(name = "per_comissao")
    private float perComissao;

    public float getPerComissao() {
        return perComissao;
    }

    public void setPerComissao(float perComissao) {
        this.perComissao = perComissao;
    }
}