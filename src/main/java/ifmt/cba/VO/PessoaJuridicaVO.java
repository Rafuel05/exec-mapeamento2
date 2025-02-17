package ifmt.cba.VO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name= "pessoa_juridica")
public class PessoaJuridicaVO extends PessoaVO {

    @Column(name ="razao_social")
    private String razaoSocial;

    @Column(name = "cnpj")
    private String cnpj;

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia(){
        return this.getNome();
    }

    public void setNomeFantasia(String nome){
        this.setNome(nome);
    }
}