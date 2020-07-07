package br.com.gliceu.tela;

import br.com.gliceu.conexao.ModuloConexao;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public final class TelaViagem extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    ResultSet rs = null;
    ResultSet rs1 = null;
    String data;
    double soma = 0;
    double somaFretes;
    double saldoViagem;
    double saldoAtual;

    public TelaViagem() {
        initComponents();
        conexao = ModuloConexao.conect();
        txtSaldoViagem.setEnabled(false);
        txtSaldoAtual.setEnabled(false);
        txtSaldoAnterior1.setEnabled(false);
        //  txtSaldoAnterior.setEnabled(false);
        zerar();
    }

    private void adicionar() {
        setarSaldoAnterior();
        if (txtNumViagem.getText().isEmpty() || txtData.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Digite o número da viagem!");
            return;
        }
        String sql = "insert into viagem (numeroViagem,lancamentoData,valorAbastecimento,litrosAbastecimento,valorGorjeta,valorDespesaGeral,valorOficina,valorBateria,valorPneus,valorComissao,freteDestino,freteRetorno,saldoAnterior,kmInicial,kmFinal,valorOleoFiltro, valorPedagios, valorJuros,valorMultas,valorRetirada,valorAluguel,descricao, suspensao,consorcio)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {

            String data = formataData(txtData.getText());
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            pst.setString(2, data);
            pst.setString(3, (txtAbastecimento.getText().replace(".", "").replace(",", ".")));
            pst.setString(4, (txtLitros.getText().replace(".", "").replace(",", ".")));
            pst.setString(5, (txtGorjeta.getText().replace(".", "").replace(",", ".")));
            pst.setString(6, (txtDespesa.getText().replace(".", "").replace(",", ".")));
            pst.setString(7, (txtOficina.getText().replace(".", "").replace(",", ".")));
            pst.setString(8, (txtBateria.getText().replace(".", "").replace(",", ".")));
            pst.setString(9, (txtPneus.getText().replace(".", "").replace(",", ".")));
            pst.setString(10, (txtComissao.getText().replace(".", "").replace(",", ".")));
            pst.setString(11, (txtFreteDestino.getText().replace(".", "").replace(",", ".")));
            pst.setString(12, (txtFreteRetorno.getText().replace(".", "").replace(",", ".")));
            pst.setString(13, (txtSaldoAnterior.getText().replace(".", "").replace(",", ".")));
            pst.setString(14, (txtKmInicial.getText().replace(".", "").replace(",", ".")));
            pst.setString(15, (txtKmFinal.getText().replace(".", "").replace(",", ".")));
            pst.setString(16, (txtOleoFiltro.getText().replace(".", "").replace(",", ".")));
            pst.setString(17, (txtPedagios.getText().replace(".", "").replace(",", ".")));
            pst.setString(18, (txtJuros.getText().replace(".", "").replace(",", ".")));
            pst.setString(19, (txtMultas.getText().replace(".", "").replace(",", ".")));
            pst.setString(20, (txtRetirada.getText().replace(".", "").replace(",", ".")));
            pst.setString(21, (txtAluguel.getText().replace(".", "").replace(",", ".")));
            pst.setString(22, (txtDescricao.getText().replace(".", "").replace(",", ".")));
            pst.setString(23, (txtSuspencao.getText().replace(".", "").replace(",", ".")));
            pst.setString(24, (txtConsorcio.getText().replace(".", "").replace(",", ".")));

            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Item adicionado com sucesso");
                setarDespesas();
                zerar();
            } else {
                JOptionPane.showMessageDialog(null, "ERRO!!!!");
                zerar();
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /* *************************************************************************** */
    public void calcularGorjeta() {
        String sql = "SELECT SUM(valorGorjeta) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalGorjeta.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void calcularAbastecimento() {
        String sql = "SELECT SUM(valorAbastecimento) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalAbastecimento.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void calcularLitros() {
        String sql = "SELECT SUM(litrosAbastecimento) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalLitros.setText(resultado);

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void calcularFrete() {
        String sql = "SELECT SUM(freteDestino) FROM viagem where numeroViagem=?";
        String sql1 = "SELECT SUM(freteRetorno) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst1 = conexao.prepareStatement(sql1);
            pst.setString(1, txtNumViagem.getText());
            pst1.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            rs1 = pst1.executeQuery();
            while (rs.next()) {
                double somaDestino = rs.getDouble(1);
                txtTotalFreteDestino.setText(String.valueOf(somaDestino));
                while (rs1.next()) {
                    double somaRetorno = rs1.getDouble(1);
                    txtTotalFreteRetorno.setText(String.valueOf(somaRetorno));
                    somaFretes = somaDestino + somaRetorno;
                    String resultado = String.format("%.2f", somaFretes);
                    txtTotalFretes.setText(resultado);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void calcularKm() {
        soma = 0;
        String sql = "SELECT SUM(kmInicial) FROM viagem where numeroViagem=?";
        String sql1 = "SELECT SUM(kmFinal) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst1 = conexao.prepareStatement(sql1);
            pst.setString(1, txtNumViagem.getText());
            pst1.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            rs1 = pst1.executeQuery();
            while (rs.next()) {
                double kmInicio = rs.getDouble(1);
                txtKmInicial1.setText(String.valueOf(kmInicio));
                while (rs1.next()) {
                    double kmFim = rs1.getDouble(1);
                    txtKmFinal1.setText(String.valueOf(kmFim));
                    double diferencaKm = kmFim - kmInicio;
                    txtTotalKmRodados.setText(String.valueOf(diferencaKm));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void calcularComissao() {
        String sql = "SELECT SUM(valorComissao) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalComissao.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void calcularOficina() {
        String sql = "SELECT SUM(valorOficina) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalOficina.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void calcularDespesaGeral() {
        String sql = "SELECT SUM(valorDespesaGeral) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalDespesasGeral.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void calcularBateria() {
        String sql = "SELECT SUM(valorBateria) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalBateria.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void calcularPneus() {
        String sql = "SELECT SUM(valorPneus) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalPneus.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /* ********************************************************************* */
    public void calcularMultas() {
        String sql = "SELECT SUM(valorMultas) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalMultas.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /* *************************************************************************** */
    public void calcularRetirada() {
        String sql = "SELECT SUM(valorRetirada) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalRetirada.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /* *************************************************************************** */
    public void calcularAluguel() {
        String sql = "SELECT SUM(valorAluguel) FROM viagem where numeroViagem=?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalAluguel.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
                //  teste(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /* *************************************************************************** */
    public void calcularOleoFiltro() {
        String sql = "SELECT SUM(valorOleoFiltro) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalOleoFiltro.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /* *************************************************************************** */
    public void calcularPedagios() {
        String sql = "SELECT SUM(valorPedagios) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalPedagios.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /* *************************************************************************** */
    public void calcularJuros() {
        String sql = "SELECT SUM(valorJuros) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalJuros.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /* *************************************************************************** */
    public void calcularSuspensao() {
        String sql = "SELECT SUM(suspensao) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalSuspencao.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /* *************************************************************************** */
    public void calcularConsorcio() {
        String sql = "SELECT SUM(consorcio) FROM viagem where numeroViagem=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                double total = rs.getDouble(1);
                String resultado = String.format("%.2f", total);
                txtTotalConsorcio.setText(String.valueOf(resultado));
                calcularTotalDespesa(total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /* *************************************************************************** */
    public void calcularSaldoAnterior() {
        String sql = "SELECT SUM(saldoAnterior) FROM viagem where numeroViagem=?";
        try {
            double total = 0;
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                total = rs.getDouble(1);
                saldoAtual = total + saldoViagem;
                String resultado1 = String.format("%.2f", total);
                String resultado = String.format("%.2f", saldoAtual);
                txtSaldoAnterior1.setText(String.valueOf(resultado1));
                txtSaldoAtual.setText(resultado);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /* ************************************************************************ */
    public void setarSaldoAnterior() {
        String sql = "SELECT SUM(saldoAnterior) FROM viagem where numeroViagem=?";
        try {
            double total = 0;
            double a = Double.parseDouble(txtSaldoAnterior.getText());
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumViagem.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                total = rs.getDouble(1);
            }
            if (total != 0) {
                if (a == 0) {
                    txtSaldoAnterior.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Saldo anterior contém registro!");
                    txtSaldoAnterior.setText("0.00");
                    txtSaldoAnterior.setEnabled(false);
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    /* ************************************************************************ */
    public void calcularTotalDespesa(double somaDespesas) {
        soma = soma + somaDespesas;
        String resultado1 = String.format("%.2f", soma);
        txtDespesaTotal.setText(String.valueOf(resultado1));
        saldoViagem = somaFretes - soma;
        String resultado = String.format("%.2f", saldoViagem);
        txtSaldoViagem.setText(resultado);
    }

    /* **********************************************************************************************/
    public void imprimirViagem() throws IOException {
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma impressao do relatório?", "ATENÇÃO", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                HashMap filtro = new HashMap();
                filtro.put("var2", Integer.parseInt(txtNumViagem.getText()));
                InputStream file1 = getClass().getResourceAsStream("/aviagem.jrxml");
                JasperDesign jasperDesign = JRXmlLoader.load(file1);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, filtro, conexao);
                JasperExportManager.exportReportToPdfFile(jasperPrint, "aviagem.pdf");
                File file2 = new File("aviagem.pdf");
                Desktop.getDesktop().open(file2);
                // JasperViewer.viewReport(jasperPrint, false);

                filtro.put("var3", Integer.parseInt(txtNumViagem.getText()));
                InputStream file3 = getClass().getResourceAsStream("/aviagem1.jrxml");
                JasperDesign jasperDesign1 = JRXmlLoader.load(file3);
                JasperReport jasperReport1 = JasperCompileManager.compileReport(jasperDesign1);
                JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperReport1, filtro, conexao);
                JasperExportManager.exportReportToPdfFile(jasperPrint1, "aviagem1.pdf");
                File file4 = new File("aviagem1.pdf");
                Desktop.getDesktop().open(file4);
                //  JasperViewer.viewReport(jasperPrint, false);
            } catch (NumberFormatException | JRException e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }
    }

    /* ***********************************************************************************************/
    public void limpar() {
        txtNumViagem.setText(null);
        txtData.setText(null);
        txtAbastecimento.setText(null);
        txtLitros.setText(null);
        txtGorjeta.setText(null);
        txtDespesa.setText(null);
        txtOficina.setText(null);
        txtBateria.setText(null);
        txtComissao.setText(null);
        txtPneus.setText(null);
        txtFreteDestino.setText(null);
        txtFreteRetorno.setText(null);
        txtSaldoAnterior.setText(null);
        txtKmInicial.setText(null);
        txtKmFinal.setText(null);
        txtDescricao.setText("");
        txtOleoFiltro.setText(null);
        txtPedagios.setText(null);
        txtJuros.setText(null);
        txtMultas.setText(null);;
        txtRetirada.setText(null);
        txtAluguel.setText(null);
        txtSuspencao.setText(null);
        txtConsorcio.setText(null);
    }

    private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date);
    }

    public void zerar() {
        conexao = ModuloConexao.conect();
        txtData.setText(getDateTime());
        txtAbastecimento.setText("0.00");
        txtLitros.setText("0.00");
        txtGorjeta.setText("0.00");
        txtDespesa.setText("0.00");
        txtOficina.setText("0.00");
        txtBateria.setText("0.00");
        txtPneus.setText("0.00");
        txtComissao.setText("0.00");
        txtFreteDestino.setText("0.00");
        txtFreteRetorno.setText("0.00");
        txtSaldoAnterior.setText("0.00");;
        txtKmInicial.setText("0.00");
        txtKmFinal.setText("0.00");
        txtDescricao.setText("");
        txtOleoFiltro.setText("0.00");
        txtPedagios.setText("0.00");
        txtJuros.setText("0.00");
        txtMultas.setText("0.00");;
        txtRetirada.setText("0.00");
        txtAluguel.setText("0.00");
        txtSuspencao.setText("0.00");
        txtConsorcio.setText("0.00");
    }

    public void setarDespesas() {
        calcularFrete();
        calcularGorjeta();
        calcularAbastecimento();
        calcularComissao();
        calcularOficina();
        calcularDespesaGeral();
        calcularBateria();
        calcularPneus();
        calcularMultas();
        calcularRetirada();
        calcularAluguel();
        calcularOleoFiltro();
        calcularPedagios();
        calcularJuros();
        calcularSuspensao();
        calcularConsorcio();
        calcularKm();
        calcularLitros();

        calcularSaldoAnterior();

        // calcular(valorPedagios);
    }

    public String formataData(String data) {
        String dia = data.substring(0, 2);
        String mes = data.substring(3, 5);
        String ano = data.substring(6);
        String yyyymmdd = ano + "-" + mes + "-" + dia;
        return yyyymmdd;
    }

    public static String formataMoeda(double d) {
        String s;
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("pt", "BR"));
        // Formato com sinal de menos -5.000,00
        // DecimalFormat df1 = new DecimalFormat("#,##0.00", dfs);

        DecimalFormat df2 = new DecimalFormat("#,##0.00;(#,##0.00)", dfs);
        // s = df1.format(d);
        // System.out.println(s);
        s = df2.format(d);
        return s;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel37 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescricao = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNumViagem = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtData = new javax.swing.JFormattedTextField();
        jLabel25 = new javax.swing.JLabel();
        txtKmInicial = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtKmFinal = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtLitros = new javax.swing.JTextField();
        txtAbastecimento = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtGorjeta = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDespesa = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtOficina = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtBateria = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtComissao = new javax.swing.JTextField();
        txtPneus = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtSaldoAnterior = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtOleoFiltro = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtPedagios = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtJuros = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtMultas = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtRetirada = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtAluguel = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtSuspencao = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtConsorcio = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        txtTotalLitros = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtTotalAbastecimento = new javax.swing.JTextField();
        txtTotalKmRodados = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtTotalFreteDestino = new javax.swing.JTextField();
        txtTotalFreteRetorno = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtTotalDespesas = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtTotalBateria = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        txtTotalPneus = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        txtTotalGorjeta = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        txtTotalOficina = new javax.swing.JTextField();
        txtKmInicial1 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txtKmFinal1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        txtTotalOleoFiltro = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        txtTotalPedagios = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        txtTotalJuros = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        txtTotalMultas = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        txtTotalRetirada = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtTotalAluguel = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        txtTotalSuspencao = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        txtTotalConsorcio = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtTotalDespesasGeral = new javax.swing.JTextField();
        txtTotalComissao = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        txtTotalFretes = new javax.swing.JTextField();
        txtDespesaTotal = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        txtFreteRetorno = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtFreteDestino = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        btnAdivionar = new javax.swing.JButton();
        btnPesquisar = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        txtSaldoAnterior1 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtSaldoViagem = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtSaldoAtual = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Minha viagem");
        getContentPane().setLayout(null);

        jLabel37.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel37.setText("Descriçao");
        jLabel37.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel37.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel37.setVerifyInputWhenFocusTarget(false);
        getContentPane().add(jLabel37);
        jLabel37.setBounds(20, 520, 110, 25);

        txtDescricao.setBackground(new java.awt.Color(234, 247, 249));
        txtDescricao.setColumns(20);
        txtDescricao.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDescricao.setRows(5);
        jScrollPane1.setViewportView(txtDescricao);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 540, 450, 80);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel1.setBackground(new java.awt.Color(255, 255, 204));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jLabel1.setText("Nº da Viagem");
        jLabel1.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 25));
        jLabel1.setVerifyInputWhenFocusTarget(false);

        txtNumViagem.setBackground(new java.awt.Color(204, 255, 255));
        txtNumViagem.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtNumViagem.setPreferredSize(new java.awt.Dimension(90, 25));
        txtNumViagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumViagemActionPerformed(evt);
            }
        });

        jLabel21.setBackground(new java.awt.Color(255, 255, 204));
        jLabel21.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 102, 102));
        jLabel21.setText("Data lançamento");
        jLabel21.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel21.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel21.setVerifyInputWhenFocusTarget(false);

        txtData.setBackground(new java.awt.Color(204, 255, 255));
        try {
            txtData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtData.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtData.setPreferredSize(new java.awt.Dimension(90, 30));

        jLabel25.setBackground(new java.awt.Color(255, 255, 204));
        jLabel25.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 102, 102));
        jLabel25.setText("Km inicial");
        jLabel25.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel25.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel25.setVerifyInputWhenFocusTarget(false);

        txtKmInicial.setBackground(new java.awt.Color(204, 255, 255));
        txtKmInicial.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtKmInicial.setPreferredSize(new java.awt.Dimension(90, 25));
        txtKmInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKmInicialActionPerformed(evt);
            }
        });

        jLabel31.setBackground(new java.awt.Color(255, 255, 204));
        jLabel31.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 102, 102));
        jLabel31.setText("Km Final");
        jLabel31.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel31.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel31.setVerifyInputWhenFocusTarget(false);

        txtKmFinal.setBackground(new java.awt.Color(204, 255, 255));
        txtKmFinal.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtKmFinal.setPreferredSize(new java.awt.Dimension(90, 25));
        txtKmFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKmFinalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNumViagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtKmInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtKmFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 8, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtKmFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtKmInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNumViagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(20, 10, 970, 40);

        jPanel2.setBackground(new java.awt.Color(255, 255, 153));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 255));
        jLabel2.setText("Abastecimento");
        jLabel2.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel2.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel2.setVerifyInputWhenFocusTarget(false);

        txtLitros.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtLitros.setPreferredSize(new java.awt.Dimension(90, 25));
        txtLitros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLitrosActionPerformed(evt);
            }
        });

        txtAbastecimento.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtAbastecimento.setPreferredSize(new java.awt.Dimension(90, 25));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 255));
        jLabel8.setText(" Litros");
        jLabel8.setPreferredSize(new java.awt.Dimension(80, 25));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAbastecimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtLitros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAbastecimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLitros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2);
        jPanel2.setBounds(20, 110, 220, 90);

        jPanel3.setBackground(new java.awt.Color(204, 255, 255));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 255));
        jLabel3.setText("Gorjeta");
        jLabel3.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel3.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel3.setVerifyInputWhenFocusTarget(false);

        txtGorjeta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtGorjeta.setPreferredSize(new java.awt.Dimension(90, 25));
        txtGorjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGorjetaActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 255));
        jLabel6.setText("Desp. Geral");
        jLabel6.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel6.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel6.setVerifyInputWhenFocusTarget(false);

        txtDespesa.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtDespesa.setPreferredSize(new java.awt.Dimension(90, 25));
        txtDespesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDespesaActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 255));
        jLabel10.setText("Oficina");
        jLabel10.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel10.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel10.setVerifyInputWhenFocusTarget(false);

        txtOficina.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtOficina.setPreferredSize(new java.awt.Dimension(90, 25));
        txtOficina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOficinaActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 255));
        jLabel12.setText("Bateria");
        jLabel12.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel12.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel12.setVerifyInputWhenFocusTarget(false);

        txtBateria.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtBateria.setPreferredSize(new java.awt.Dimension(90, 25));
        txtBateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBateriaActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 255));
        jLabel14.setText("Comissão");
        jLabel14.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel14.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel14.setVerifyInputWhenFocusTarget(false);

        txtComissao.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtComissao.setPreferredSize(new java.awt.Dimension(90, 25));
        txtComissao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtComissaoActionPerformed(evt);
            }
        });

        txtPneus.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtPneus.setPreferredSize(new java.awt.Dimension(90, 25));
        txtPneus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPneusActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 51, 255));
        jLabel22.setText("Pneus");
        jLabel22.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel22.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel22.setVerifyInputWhenFocusTarget(false);

        txtSaldoAnterior.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtSaldoAnterior.setPreferredSize(new java.awt.Dimension(90, 25));
        txtSaldoAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaldoAnteriorActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 51, 255));
        jLabel27.setText("Saldo Anterior");
        jLabel27.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel27.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel27.setVerifyInputWhenFocusTarget(false);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 255));
        jLabel5.setText("Óleo e Filtros");
        jLabel5.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel5.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel5.setVerifyInputWhenFocusTarget(false);

        txtOleoFiltro.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtOleoFiltro.setPreferredSize(new java.awt.Dimension(90, 25));
        txtOleoFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOleoFiltroActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 255));
        jLabel13.setText("Pedágios");
        jLabel13.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel13.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel13.setVerifyInputWhenFocusTarget(false);

        txtPedagios.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtPedagios.setPreferredSize(new java.awt.Dimension(90, 25));
        txtPedagios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPedagiosActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 255));
        jLabel7.setText("Juros");
        jLabel7.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel7.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel7.setVerifyInputWhenFocusTarget(false);

        txtJuros.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtJuros.setPreferredSize(new java.awt.Dimension(90, 25));
        txtJuros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJurosActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 255));
        jLabel15.setText("Multas");
        jLabel15.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel15.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel15.setVerifyInputWhenFocusTarget(false);

        txtMultas.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtMultas.setPreferredSize(new java.awt.Dimension(90, 25));
        txtMultas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMultasActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 255));
        jLabel9.setText("Retirada");
        jLabel9.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel9.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel9.setVerifyInputWhenFocusTarget(false);

        txtRetirada.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtRetirada.setPreferredSize(new java.awt.Dimension(90, 25));
        txtRetirada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRetiradaActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 255));
        jLabel16.setText("Aluguel");
        jLabel16.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel16.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel16.setVerifyInputWhenFocusTarget(false);

        txtAluguel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtAluguel.setPreferredSize(new java.awt.Dimension(90, 25));
        txtAluguel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAluguelActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 255));
        jLabel11.setText("Suspensão");
        jLabel11.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel11.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel11.setVerifyInputWhenFocusTarget(false);

        txtSuspencao.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtSuspencao.setPreferredSize(new java.awt.Dimension(90, 25));
        txtSuspencao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSuspencaoActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 255));
        jLabel18.setText("Consórcio");
        jLabel18.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel18.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel18.setVerifyInputWhenFocusTarget(false);

        txtConsorcio.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtConsorcio.setPreferredSize(new java.awt.Dimension(90, 25));
        txtConsorcio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtConsorcioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSaldoAnterior, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSuspencao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRetirada, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJuros, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOleoFiltro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOficina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDespesa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGorjeta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtConsorcio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAluguel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMultas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPedagios, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPneus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtComissao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBateria, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGorjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtComissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtOficina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPneus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPedagios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtOleoFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMultas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtAluguel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtRetirada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtConsorcio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSuspencao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSaldoAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3);
        jPanel3.setBounds(20, 210, 450, 300);

        jPanel4.setBackground(new java.awt.Color(153, 255, 204));

        jLabel24.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel24.setText("Quant. litros");
        jLabel24.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel24.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel24.setVerifyInputWhenFocusTarget(false);

        txtTotalLitros.setEditable(false);
        txtTotalLitros.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalLitros.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalLitros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalLitrosActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel26.setText("Valor abast.");
        jLabel26.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel26.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel26.setVerifyInputWhenFocusTarget(false);

        txtTotalAbastecimento.setEditable(false);
        txtTotalAbastecimento.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalAbastecimento.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalAbastecimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalAbastecimentoActionPerformed(evt);
            }
        });

        txtTotalKmRodados.setEditable(false);
        txtTotalKmRodados.setBackground(new java.awt.Color(205, 255, 255));
        txtTotalKmRodados.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalKmRodados.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalKmRodados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalKmRodadosActionPerformed(evt);
            }
        });

        jLabel34.setBackground(new java.awt.Color(0, 0, 204));
        jLabel34.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 102, 255));
        jLabel34.setText("Rodados");
        jLabel34.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel34.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel34.setVerifyInputWhenFocusTarget(false);

        jLabel35.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel35.setText("Frete Destino");
        jLabel35.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel35.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel35.setVerifyInputWhenFocusTarget(false);

        txtTotalFreteDestino.setEditable(false);
        txtTotalFreteDestino.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalFreteDestino.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalFreteDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalFreteDestinoActionPerformed(evt);
            }
        });

        txtTotalFreteRetorno.setEditable(false);
        txtTotalFreteRetorno.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalFreteRetorno.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalFreteRetorno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalFreteRetornoActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel39.setText("Frete retorno");
        jLabel39.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel39.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel39.setVerifyInputWhenFocusTarget(false);

        txtTotalDespesas.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalDespesas.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalDespesas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalDespesasActionPerformed(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel43.setText("Total despesas");
        jLabel43.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel43.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel43.setVerifyInputWhenFocusTarget(false);

        txtTotalBateria.setEditable(false);
        txtTotalBateria.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalBateria.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalBateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalBateriaActionPerformed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel45.setText("Bateria");
        jLabel45.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel45.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel45.setVerifyInputWhenFocusTarget(false);

        jLabel46.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel46.setText("Pneus");
        jLabel46.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel46.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel46.setVerifyInputWhenFocusTarget(false);

        txtTotalPneus.setEditable(false);
        txtTotalPneus.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalPneus.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalPneus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalPneusActionPerformed(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel47.setText("Oficina");
        jLabel47.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel47.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel47.setVerifyInputWhenFocusTarget(false);

        jLabel49.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel49.setText("Gorjeta");
        jLabel49.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel49.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel49.setVerifyInputWhenFocusTarget(false);

        txtTotalGorjeta.setEditable(false);
        txtTotalGorjeta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalGorjeta.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalGorjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalGorjetaActionPerformed(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel51.setText("Oficina");
        jLabel51.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel51.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel51.setVerifyInputWhenFocusTarget(false);

        txtTotalOficina.setEditable(false);
        txtTotalOficina.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalOficina.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalOficina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalOficinaActionPerformed(evt);
            }
        });

        txtKmInicial1.setEditable(false);
        txtKmInicial1.setBackground(new java.awt.Color(204, 255, 255));
        txtKmInicial1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtKmInicial1.setPreferredSize(new java.awt.Dimension(90, 25));
        txtKmInicial1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKmInicial1ActionPerformed(evt);
            }
        });

        jLabel33.setBackground(new java.awt.Color(0, 0, 204));
        jLabel33.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 102, 255));
        jLabel33.setText("Inicial");
        jLabel33.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel33.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel33.setVerifyInputWhenFocusTarget(false);

        jLabel36.setBackground(new java.awt.Color(0, 102, 255));
        jLabel36.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 102, 255));
        jLabel36.setText("Final");
        jLabel36.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel36.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel36.setVerifyInputWhenFocusTarget(false);

        txtKmFinal1.setEditable(false);
        txtKmFinal1.setBackground(new java.awt.Color(204, 255, 255));
        txtKmFinal1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtKmFinal1.setPreferredSize(new java.awt.Dimension(90, 25));
        txtKmFinal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKmFinal1ActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(0, 0, 255));
        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 0, 255));
        jLabel4.setText("Quilometragem");

        jLabel52.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel52.setText("Óleo e Filtros");
        jLabel52.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel52.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel52.setVerifyInputWhenFocusTarget(false);

        txtTotalOleoFiltro.setEditable(false);
        txtTotalOleoFiltro.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalOleoFiltro.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalOleoFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalOleoFiltroActionPerformed(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel53.setText("Pedágios");
        jLabel53.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel53.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel53.setVerifyInputWhenFocusTarget(false);

        txtTotalPedagios.setEditable(false);
        txtTotalPedagios.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalPedagios.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalPedagios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalPedagiosActionPerformed(evt);
            }
        });

        jLabel54.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel54.setText("Juros");
        jLabel54.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel54.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel54.setVerifyInputWhenFocusTarget(false);

        txtTotalJuros.setEditable(false);
        txtTotalJuros.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalJuros.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalJuros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalJurosActionPerformed(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel55.setText("Multas");
        jLabel55.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel55.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel55.setVerifyInputWhenFocusTarget(false);

        txtTotalMultas.setEditable(false);
        txtTotalMultas.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalMultas.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalMultas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalMultasActionPerformed(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel56.setText("Retirada");
        jLabel56.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel56.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel56.setVerifyInputWhenFocusTarget(false);

        txtTotalRetirada.setEditable(false);
        txtTotalRetirada.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalRetirada.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalRetirada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalRetiradaActionPerformed(evt);
            }
        });

        jLabel57.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel57.setText("Aluguel");
        jLabel57.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel57.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel57.setVerifyInputWhenFocusTarget(false);

        txtTotalAluguel.setEditable(false);
        txtTotalAluguel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalAluguel.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalAluguel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalAluguelActionPerformed(evt);
            }
        });

        jLabel58.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel58.setText("Suspencao");
        jLabel58.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel58.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel58.setVerifyInputWhenFocusTarget(false);

        txtTotalSuspencao.setEditable(false);
        txtTotalSuspencao.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalSuspencao.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalSuspencao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalSuspencaoActionPerformed(evt);
            }
        });

        jLabel59.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel59.setText("Consórcio");
        jLabel59.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel59.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel59.setVerifyInputWhenFocusTarget(false);

        txtTotalConsorcio.setEditable(false);
        txtTotalConsorcio.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalConsorcio.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalConsorcio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalConsorcioActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel41.setText("Desp. Gerais");
        jLabel41.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel41.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel41.setVerifyInputWhenFocusTarget(false);

        txtTotalDespesasGeral.setEditable(false);
        txtTotalDespesasGeral.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalDespesasGeral.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalDespesasGeral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalDespesasGeralActionPerformed(evt);
            }
        });

        txtTotalComissao.setEditable(false);
        txtTotalComissao.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalComissao.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalComissao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalComissaoActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel44.setText("Comissão");
        jLabel44.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel44.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel44.setVerifyInputWhenFocusTarget(false);

        jLabel48.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel48.setText("Total Fretes");
        jLabel48.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel48.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel48.setVerifyInputWhenFocusTarget(false);

        jLabel50.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel50.setText("Total Desp.");
        jLabel50.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel50.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel50.setVerifyInputWhenFocusTarget(false);

        txtTotalFretes.setEditable(false);
        txtTotalFretes.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTotalFretes.setPreferredSize(new java.awt.Dimension(90, 25));
        txtTotalFretes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalFretesActionPerformed(evt);
            }
        });

        txtDespesaTotal.setEditable(false);
        txtDespesaTotal.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtDespesaTotal.setPreferredSize(new java.awt.Dimension(90, 25));
        txtDespesaTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDespesaTotalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addGap(14, 14, 14))
                            .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotalFreteDestino, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotalFreteRetorno, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotalAbastecimento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotalLitros, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotalOleoFiltro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotalJuros, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotalRetirada, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotalSuspencao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotalDespesasGeral, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotalComissao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                    .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                    .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                                .addGap(12, 12, 12)))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotalBateria, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(txtTotalPneus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(txtTotalGorjeta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(txtTotalOficina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(txtTotalPedagios, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(txtTotalMultas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(txtTotalAluguel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(txtTotalFretes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotalConsorcio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(txtDespesaTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(234, 234, 234)
                        .addComponent(txtTotalDespesas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtKmInicial1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtKmFinal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotalKmRodados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(14, 14, 14))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTotalFreteDestino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(7, 7, 7)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTotalFreteRetorno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTotalAbastecimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTotalLitros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(6, 6, 6)
                                .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(6, 6, 6)
                                .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(6, 6, 6)
                                .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtTotalBateria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6)
                        .addComponent(txtTotalPneus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6)
                        .addComponent(txtTotalGorjeta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6)
                        .addComponent(txtTotalOficina, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalPedagios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTotalOleoFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(7, 7, 7)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalMultas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTotalJuros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalAluguel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTotalRetirada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalConsorcio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTotalSuspencao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalDespesasGeral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotalComissao, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(15, 15, 15)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(txtTotalFretes, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDespesaTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(41, 41, 41)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtKmFinal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtKmInicial1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTotalKmRodados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(148, 148, 148)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalDespesas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(jPanel4);
        jPanel4.setBounds(510, 100, 480, 430);

        jPanel6.setBackground(new java.awt.Color(204, 204, 255));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel28.setText("Frete Retorno");
        jLabel28.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel28.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel28.setVerifyInputWhenFocusTarget(false);

        txtFreteRetorno.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtFreteRetorno.setPreferredSize(new java.awt.Dimension(90, 25));
        txtFreteRetorno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFreteRetornoActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel30.setText("Frete Destino");
        jLabel30.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel30.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel30.setVerifyInputWhenFocusTarget(false);

        txtFreteDestino.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtFreteDestino.setPreferredSize(new java.awt.Dimension(90, 25));
        txtFreteDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFreteDestinoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFreteDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFreteRetorno, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFreteDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFreteRetorno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel6);
        jPanel6.setBounds(250, 110, 220, 90);

        btnCancelar.setBackground(new java.awt.Color(153, 255, 102));
        btnCancelar.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(0, 153, 204));
        btnCancelar.setText("Imprimir");
        btnCancelar.setMaximumSize(new java.awt.Dimension(115, 200));
        btnCancelar.setPreferredSize(new java.awt.Dimension(30, 110));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancelar);
        btnCancelar.setBounds(1020, 230, 120, 50);

        jPanel7.setBackground(new java.awt.Color(255, 255, 153));
        jPanel7.setName(""); // NOI18N

        jLabel20.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 0, 153));
        jLabel20.setText("Resumo");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(183, 183, 183)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(197, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel7);
        jPanel7.setBounds(510, 60, 480, 41);

        btnAdivionar.setBackground(new java.awt.Color(153, 255, 102));
        btnAdivionar.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnAdivionar.setForeground(new java.awt.Color(0, 153, 204));
        btnAdivionar.setText("Adicionar");
        btnAdivionar.setMaximumSize(new java.awt.Dimension(115, 200));
        btnAdivionar.setPreferredSize(new java.awt.Dimension(30, 110));
        btnAdivionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdivionarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdivionar);
        btnAdivionar.setBounds(1020, 110, 120, 50);

        btnPesquisar.setBackground(new java.awt.Color(153, 255, 102));
        btnPesquisar.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnPesquisar.setForeground(new java.awt.Color(0, 153, 204));
        btnPesquisar.setText("Atualizar");
        btnPesquisar.setMaximumSize(new java.awt.Dimension(115, 200));
        btnPesquisar.setPreferredSize(new java.awt.Dimension(30, 110));
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });
        getContentPane().add(btnPesquisar);
        btnPesquisar.setBounds(1020, 170, 120, 50);

        jPanel8.setBackground(new java.awt.Color(0, 204, 204));

        txtSaldoAnterior1.setEditable(false);
        txtSaldoAnterior1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtSaldoAnterior1.setPreferredSize(new java.awt.Dimension(90, 25));
        txtSaldoAnterior1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaldoAnterior1ActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel32.setText("Saldo Anterior");
        jLabel32.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel32.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel32.setVerifyInputWhenFocusTarget(false);

        txtSaldoViagem.setEditable(false);
        txtSaldoViagem.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtSaldoViagem.setPreferredSize(new java.awt.Dimension(90, 25));
        txtSaldoViagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaldoViagemActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel17.setText("Saldo Viagem");
        jLabel17.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel17.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel17.setVerifyInputWhenFocusTarget(false);

        jLabel19.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel19.setText("Saldo Atual");
        jLabel19.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel19.setPreferredSize(new java.awt.Dimension(110, 25));
        jLabel19.setVerifyInputWhenFocusTarget(false);

        txtSaldoAtual.setEditable(false);
        txtSaldoAtual.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtSaldoAtual.setPreferredSize(new java.awt.Dimension(90, 25));
        txtSaldoAtual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaldoAtualActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtSaldoAnterior1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSaldoAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSaldoViagem, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSaldoAnterior1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtSaldoAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSaldoViagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel8);
        jPanel8.setBounds(510, 530, 480, 90);

        jPanel9.setBackground(new java.awt.Color(255, 255, 153));
        jPanel9.setName(""); // NOI18N

        jLabel38.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(51, 0, 153));
        jLabel38.setText("Lançamentos");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(150, Short.MAX_VALUE)
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(137, 137, 137))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel9);
        jPanel9.setBounds(20, 60, 454, 41);

        setSize(new java.awt.Dimension(1179, 679));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNumViagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumViagemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumViagemActionPerformed

    private void txtGorjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGorjetaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGorjetaActionPerformed

    private void txtDespesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDespesaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDespesaActionPerformed

    private void txtOficinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOficinaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOficinaActionPerformed

    private void txtBateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBateriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBateriaActionPerformed

    private void txtComissaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtComissaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtComissaoActionPerformed

    private void txtSaldoViagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaldoViagemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaldoViagemActionPerformed

    private void txtSaldoAtualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaldoAtualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaldoAtualActionPerformed

    private void txtTotalLitrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalLitrosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalLitrosActionPerformed

    private void txtTotalAbastecimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalAbastecimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalAbastecimentoActionPerformed

    private void txtSaldoAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaldoAnteriorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaldoAnteriorActionPerformed

    private void txtFreteRetornoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFreteRetornoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFreteRetornoActionPerformed

    private void txtFreteDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFreteDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFreteDestinoActionPerformed

    private void txtTotalKmRodadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalKmRodadosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalKmRodadosActionPerformed

    private void txtTotalFreteDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalFreteDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalFreteDestinoActionPerformed

    private void txtTotalFreteRetornoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalFreteRetornoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalFreteRetornoActionPerformed

    private void txtTotalDespesasGeralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalDespesasGeralActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalDespesasGeralActionPerformed

    private void txtTotalDespesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalDespesasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalDespesasActionPerformed

    private void txtPneusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPneusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPneusActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        try {
            imprimirViagem();
        } catch (IOException ex) {
            Logger.getLogger(TelaViagem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        setarDespesas();
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void txtKmInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKmInicialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmInicialActionPerformed

    private void txtKmFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKmFinalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmFinalActionPerformed

    private void txtTotalComissaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalComissaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalComissaoActionPerformed

    private void txtLitrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLitrosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLitrosActionPerformed

    private void btnAdivionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdivionarActionPerformed
        adicionar();
    }//GEN-LAST:event_btnAdivionarActionPerformed

    private void txtTotalBateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalBateriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalBateriaActionPerformed

    private void txtTotalPneusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalPneusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalPneusActionPerformed

    private void txtTotalFretesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalFretesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalFretesActionPerformed

    private void txtTotalGorjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalGorjetaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalGorjetaActionPerformed

    private void txtTotalOficinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalOficinaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalOficinaActionPerformed

    private void txtDespesaTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDespesaTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDespesaTotalActionPerformed

    private void txtKmInicial1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKmInicial1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmInicial1ActionPerformed

    private void txtKmFinal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKmFinal1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmFinal1ActionPerformed

    private void txtSaldoAnterior1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaldoAnterior1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaldoAnterior1ActionPerformed

    private void txtOleoFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOleoFiltroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOleoFiltroActionPerformed

    private void txtPedagiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPedagiosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPedagiosActionPerformed

    private void txtJurosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJurosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJurosActionPerformed

    private void txtMultasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMultasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMultasActionPerformed

    private void txtRetiradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRetiradaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRetiradaActionPerformed

    private void txtAluguelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAluguelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAluguelActionPerformed

    private void txtTotalOleoFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalOleoFiltroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalOleoFiltroActionPerformed

    private void txtTotalPedagiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalPedagiosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalPedagiosActionPerformed

    private void txtTotalJurosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalJurosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalJurosActionPerformed

    private void txtTotalMultasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalMultasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalMultasActionPerformed

    private void txtTotalRetiradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalRetiradaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalRetiradaActionPerformed

    private void txtTotalAluguelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalAluguelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalAluguelActionPerformed

    private void txtSuspencaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSuspencaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSuspencaoActionPerformed

    private void txtConsorcioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtConsorcioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtConsorcioActionPerformed

    private void txtTotalSuspencaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalSuspencaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalSuspencaoActionPerformed

    private void txtTotalConsorcioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalConsorcioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalConsorcioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaViagem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaViagem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaViagem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaViagem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaViagem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdivionar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtAbastecimento;
    private javax.swing.JTextField txtAluguel;
    private javax.swing.JTextField txtBateria;
    private javax.swing.JTextField txtComissao;
    private javax.swing.JTextField txtConsorcio;
    private javax.swing.JFormattedTextField txtData;
    private javax.swing.JTextArea txtDescricao;
    private javax.swing.JTextField txtDespesa;
    private javax.swing.JTextField txtDespesaTotal;
    private javax.swing.JTextField txtFreteDestino;
    private javax.swing.JTextField txtFreteRetorno;
    private javax.swing.JTextField txtGorjeta;
    private javax.swing.JTextField txtJuros;
    private javax.swing.JTextField txtKmFinal;
    private javax.swing.JTextField txtKmFinal1;
    private javax.swing.JTextField txtKmInicial;
    private javax.swing.JTextField txtKmInicial1;
    private javax.swing.JTextField txtLitros;
    private javax.swing.JTextField txtMultas;
    private javax.swing.JTextField txtNumViagem;
    private javax.swing.JTextField txtOficina;
    private javax.swing.JTextField txtOleoFiltro;
    private javax.swing.JTextField txtPedagios;
    private javax.swing.JTextField txtPneus;
    private javax.swing.JTextField txtRetirada;
    private javax.swing.JTextField txtSaldoAnterior;
    private javax.swing.JTextField txtSaldoAnterior1;
    private javax.swing.JTextField txtSaldoAtual;
    private javax.swing.JTextField txtSaldoViagem;
    private javax.swing.JTextField txtSuspencao;
    private javax.swing.JTextField txtTotalAbastecimento;
    private javax.swing.JTextField txtTotalAluguel;
    private javax.swing.JTextField txtTotalBateria;
    private javax.swing.JTextField txtTotalComissao;
    private javax.swing.JTextField txtTotalConsorcio;
    private javax.swing.JTextField txtTotalDespesas;
    private javax.swing.JTextField txtTotalDespesasGeral;
    private javax.swing.JTextField txtTotalFreteDestino;
    private javax.swing.JTextField txtTotalFreteRetorno;
    private javax.swing.JTextField txtTotalFretes;
    private javax.swing.JTextField txtTotalGorjeta;
    private javax.swing.JTextField txtTotalJuros;
    private javax.swing.JTextField txtTotalKmRodados;
    private javax.swing.JTextField txtTotalLitros;
    private javax.swing.JTextField txtTotalMultas;
    private javax.swing.JTextField txtTotalOficina;
    private javax.swing.JTextField txtTotalOleoFiltro;
    private javax.swing.JTextField txtTotalPedagios;
    private javax.swing.JTextField txtTotalPneus;
    private javax.swing.JTextField txtTotalRetirada;
    private javax.swing.JTextField txtTotalSuspencao;
    // End of variables declaration//GEN-END:variables
}
