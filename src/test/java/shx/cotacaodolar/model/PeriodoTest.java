package shx.cotacaodolar.model;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PeriodoTest {
  @Test
  public void getDataInicial() throws ParseException {
    Periodo periodo = createPeriodo("02-02-2025", "02-05-2025");
    assertThat(periodo.getDataInicial()).isEqualTo("02-02-2025");
  }

  @Test
  public void getDataFinal() throws ParseException {
    Periodo periodo = createPeriodo("02-02-2025", "02-05-2025");
    assertThat(periodo.getDataFinal()).isEqualTo("02-05-2025");
  }

  @Test
  public void getDiasEntreAsDatasMaisUm() throws ParseException {
    Periodo periodo = createPeriodo("02-02-2025", "02-05-2025");
    assertThat(periodo.getDiasEntreAsDatasMaisUm()).isEqualTo("4");
  }

  @Test
  public void throwAParsingError() throws ParseException {
    assertThrows(ParseException.class, () -> {
      createPeriodo("02-02", "02-05-2025");
    });

    assertThrows(ParseException.class, () -> {
      createPeriodo("02-02-2025", "02-2025");
    });

    assertThrows(ParseException.class, () -> {
      createPeriodo("", "02-05-2025");
    });

    assertThrows(ParseException.class, () -> {
      createPeriodo("string", "02-05-2025");
    });

    assertThrows(ParseException.class, () -> {
      createPeriodo("string", "string");
    });
  }

  private Periodo createPeriodo(String dataInicial, String dataFinal) throws ParseException {
    Periodo periodo = new Periodo(dataInicial, dataFinal);
    return periodo;
  }
}
