package shx.cotacaodolar.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import shx.cotacaodolar.model.Moeda;
import shx.cotacaodolar.model.Periodo;



@Service
public class MoedaService {

    public List<Moeda> getCotacoesPeriodo(String startDate, String endDate) throws IOException, MalformedURLException, ParseException{
        Periodo periodo = new Periodo(startDate, endDate);

        String urlString = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarPeriodo(dataInicial=@dataInicial,dataFinalCotacao=@dataFinalCotacao)?%40dataInicial='" + periodo.getDataInicial() + "'&%40dataFinalCotacao='" + periodo.getDataFinal() + "'&%24format=json&%24skip=0&%24top=" + periodo.getDiasEntreAsDatasMaisUm();

        URL url = new URL(urlString);
        HttpURLConnection request = (HttpURLConnection)url.openConnection();
        request.connect();

        JsonElement response = JsonParser.parseReader(new InputStreamReader((InputStream)request.getContent()));
        JsonObject rootObj = response.getAsJsonObject();
        JsonArray cotacoesArray = rootObj.getAsJsonArray("value");

        List<Moeda> moedasLista = new ArrayList<Moeda>();

        for(JsonElement obj : cotacoesArray){
            Moeda moedaRef = new Moeda();
            Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getAsJsonObject().get("dataHoraCotacao").getAsString());

            moedaRef.preco = obj.getAsJsonObject().get("cotacaoCompra").getAsDouble();
            moedaRef.data = new SimpleDateFormat("dd/MM/yyyy").format(data);
            moedaRef.hora = new SimpleDateFormat("HH:mm:ss").format(data);
            moedasLista.add(moedaRef);
        }
        return moedasLista;
    }

    public Moeda getCotacaoAtual() throws IOException, MalformedURLException, ParseException {
        Date data = new Date();
        String pattern = "MM-dd-yyyy";
        String dataAtual = new SimpleDateFormat(pattern).format(data);

        List<Moeda> cotacoes = this.getCotacoesPeriodo(dataAtual, dataAtual);

        return getCotacaoUltimoDiaUtil(data, cotacoes, pattern);
    }

    public List<Moeda> getCotacoesMenoresAtual(String startDate, String endDate) throws IOException, MalformedURLException, ParseException {
        Moeda cotacaoAtual = this.getCotacaoAtual();
        List<Moeda> cotacoesPeriodo = this.getCotacoesPeriodo(startDate, endDate);

        List<Moeda> cotacoes =  new ArrayList<>();

        for(Moeda cotacao : cotacoesPeriodo) {
            if(cotacao.preco < cotacaoAtual.preco) {
                cotacoes.add(cotacao);
            }
        }

        return cotacoes;
    }

    private Moeda getCotacaoUltimoDiaUtil(Date date, List<Moeda> cotacoes, String pattern) throws IOException, MalformedURLException, ParseException {
        if(!cotacoes.isEmpty()) {
            return cotacoes.get(0);
        }

        date = Date.from(date.toInstant().minus(Duration.ofDays(1)));
        String dateMinusOneDay = new SimpleDateFormat(pattern).format(date);
        cotacoes = this.getCotacoesPeriodo(dateMinusOneDay, dateMinusOneDay);

        return getCotacaoUltimoDiaUtil(date, cotacoes, pattern);
    }
}
