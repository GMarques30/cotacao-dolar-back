package shx.cotacaodolar.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoedaTest {
  @Test
  public void getPreco() {
    Moeda moeda = criarMoeda(10.00, "11/04/2025", "20:30:58");

    assertThat(moeda.preco).isEqualTo(10.00);
  }

  @Test
  public void getData() {
    Moeda moeda = criarMoeda(10.00, "11/04/2025", "20:30:58");

    assertThat(moeda.data).isEqualTo("11/04/2025");
  }

  @Test
  public void getHora() {
    Moeda moeda = criarMoeda(10.00, "11/04/2025", "20:30:58");

    assertThat(moeda.hora).isEqualTo("20:30:58");
  }

  @Test
  public void precoCannotBeNull() {
    Moeda moeda = criarMoeda(null, "11/04/2025", "20:30:58");;

    assertThat(moeda.preco).isNull();
  }

  @Test
  public void precoCannotBeNegative() {
    Moeda moeda = criarMoeda(-10.00, "11/04/2025", "20:30:58");

    assertThat(moeda.preco).isEqualTo(-10.00);
  }

  @Test
  public void dataCannotBeNull() {
    Moeda moeda = criarMoeda(10.00, null, "20:30:58");

    assertThat(moeda.data).isNull();
  }

  @Test
  public void horaCannotBeNull() {
    Moeda moeda = criarMoeda(10.00, "11/04/2025", null);

    assertThat(moeda.hora).isNull();
  }

  @Test
  public void methodToString() {
    Moeda moeda = criarMoeda(10.00, "11/04/2025", "20:30:58");

    assertThat(moeda.toString()).isEqualTo("10.011/04/202520:30:58");
  }

  private Moeda criarMoeda(Double preco, String data, String hora) {
    Moeda moeda = new Moeda();
    moeda.preco = preco;
    moeda.data = data;
    moeda.hora = hora;
    return moeda;
  }
}
