package br.com.humberto.fam;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Informante{
    
    static DateTimeFormatter dtf;
   
    public static String queDiaEHoje() {
      dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      return dtf.format(LocalDate.now());
    }

    public static String queHorasSao() {
      dtf = DateTimeFormatter.ofPattern("kk:mm");
      return dtf.format(LocalDateTime.now());
    }

    public static Mensagem responder(Mensagem mensagem) {
      switch (mensagem.getTipoDaPergunta()) {
        case DATA:
          mensagem.setResposta(queDiaEHoje());
          break;
        case HORA:
          mensagem.setResposta(queHorasSao());
          break;
      }
      return mensagem;
    }
}

public class ServidorTCP {
  
  public static void main(String[] args) {
    TipoDaPergunta tipodapergunta;
    
    
    try {
      
      ServerSocket socketServidor = new ServerSocket(12345);
      while (true) {
        
        Informante informante = new Informante();
        ObjectInputStream ois;
        ObjectOutputStream oos;
        Socket cliente = socketServidor.accept();
        ois = new ObjectInputStream(
                cliente.getInputStream());
       
        Mensagem mensagemrecebida = (Mensagem) ois.readObject();
        Mensagem mensagem = informante.responder(mensagemrecebida);
        oos = new ObjectOutputStream(
                cliente.getOutputStream());
        oos.flush();
        
     
        oos.writeObject(String.format("Recebido: %s",
                mensagem.getResposta()));
        oos.close();
        ois.close();
      }
    } catch (IOException | ClassNotFoundException e) {
      System.out.println(String.format("Erro: %s",
              e.getLocalizedMessage()));
    }
  }
}
