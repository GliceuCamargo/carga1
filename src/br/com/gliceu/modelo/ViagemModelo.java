package br.com.gliceu.modelo;

import java.util.Date;

public class ViagemModelo {

    private int PKIDViagem;
    private Date dataInicio;
   // private Date dataFim;
    private double kmInicial;
    private double kmfinal;
    private double kmRodado;
    private String destino;
    private String retorno;    
   // private int FKIDMotorista;  
   // private int FKIDCaminhao;

    public int getPKIDViagem() {
        return PKIDViagem;
    }

    public void setPKIDViagem(int PKIDViagem) {
        this.PKIDViagem = PKIDViagem;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public double getKmInicial() {
        return kmInicial;
    }

    public void setKmInicial(double kmInicial) {
        this.kmInicial = kmInicial;
    }

    public double getKmfinal() {
        return kmfinal;
    }

    public void setKmfinal(double kmfinal) {
        this.kmfinal = kmfinal;
    }

    public double getKmRodado() {
        return kmRodado;
    }

    public void setKmRodado(double kmRodado) {
        this.kmRodado = kmRodado;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }
}

