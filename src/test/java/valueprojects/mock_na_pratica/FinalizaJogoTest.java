package valueprojects.mock_na_pratica;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import br.com.value.jogo.builder.CriadorDeJogo;
import dominio.Jogo;
import dominio.SMS;
import infra.JogoDao;
import service.FinalizaJogo;
import static org.mockito.Mockito.*;

public class FinalizaJogoTest {
	
	 @Test
	    public void deveFinalizarJogosDaSemanaAnterior() {

	        Calendar antiga = Calendar.getInstance();
	        antiga.set(1999, 1, 20);

	        Jogo jogo1 = new CriadorDeJogo().para("Ca�a moedas")
	            .naData(antiga).constroi();
	        Jogo jogo2 = new CriadorDeJogo().para("Derruba barreiras")
	            .naData(antiga).constroi();

	        // mock no lugar de dao falso
	        
	        List<Jogo> jogosAnteriores = Arrays.asList(jogo1, jogo2);

	        JogoDao daoFalso = mock(JogoDao.class);

	        when(daoFalso.emAndamento()).thenReturn(jogosAnteriores);

	        FinalizaJogo finalizador = new FinalizaJogo(daoFalso);
	        finalizador.finaliza();

	        assertTrue(jogo1.isFinalizado());
	        assertTrue(jogo2.isFinalizado());
	        assertEquals(2, finalizador.getTotalFinalizados());
	    }
	 
	 @Test
		public void deveVerificarSeMetodoAtualizaFoiInvocado() {

			Calendar antiga = Calendar.getInstance();
			antiga.set(1999, 1, 20);

			Jogo jogo1 = new CriadorDeJogo().para("Cata moedas").naData(antiga).constroi();
			Jogo jogo2 = new CriadorDeJogo().para("Derruba barreiras").naData(antiga).constroi();

			// mock no lugar de dao falso

			List<Jogo> jogosAnteriores = Arrays.asList(jogo1, jogo2);

			JogoDao daoFalso = mock(JogoDao.class);

			when(daoFalso.emAndamento()).thenReturn(jogosAnteriores);

			FinalizaJogo finalizador = new FinalizaJogo(daoFalso);
			finalizador.finaliza();

			verify(daoFalso, times(1)).atualiza(jogo1);
			//Mockito.verifyNoInteractions(daoFalso);
	
					
			
		}
	 
	 @Test
		public void deveEnviarSMSAposDadosSalvos() {

			Calendar antiga = Calendar.getInstance();
			antiga.set(1999, 1, 20);

			Jogo jogo = new CriadorDeJogo().para("Cata moedas").naData(antiga).constroi();	
			jogo.finaliza();
			
			SMS smsFalso = mock(SMS.class);
			smsFalso.setText("parabens vc ganhou nada");
			jogo.setSms(smsFalso);
			
			JogoDao daoFalso = mock(JogoDao.class);
			daoFalso.salva(jogo);
			
			verify(smsFalso, times(0)).setEnviado(true);
			
			verify(daoFalso, times(1)).salva(jogo);
			//verify(daoFalso, times(1)).salvaGanhador(jogo); NÃO DEU TEMPO
			
			smsFalso.setEnviado(true);
			verify(smsFalso, times(1)).setEnviado(true);
		} 
	}

 

	
	

	
