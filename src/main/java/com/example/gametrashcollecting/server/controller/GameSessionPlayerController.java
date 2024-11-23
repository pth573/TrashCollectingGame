//
//package com.example.gametrashcollecting.server.controller;
//
//import com.example.gametrashcollecting.model.GameSessionPlayer;
//import com.example.gametrashcollecting.server.dao.GameSessionPlayerDAO;
//import com.example.gametrashcollecting.server.statusReponse.ResponseStatus;
//import com.example.gametrashcollecting.server.statusReponse.Status;
//import com.example.gametrashcollecting.server.utils.DatabaseConnectionManager;
//
//public class GameSessionPlayerController {
//   private static GameSessionPlayerDAO gameSessionPlayerDAO;
//   private static DatabaseConnectionManager dbManager;
//
//   public GameSessionPlayerController(GameSessionPlayerDAO gameSessionPlayerDAO) {
//       this.gameSessionPlayerDAO = gameSessionPlayerDAO;
//       dbManager = new DatabaseConnectionManager();
//   }
//
//   public static ResponseStatus createGameSessionPlayer() throws Exception {
//       gameSessionPlayerDAO = new GameSessionPlayerDAO();
//       GameSessionPlayer gameSessionPlayer = gameSessionPlayerDAO.createGameSessionPlayer();
//       if(gameSessionPlayer != null) {
//           return new ResponseStatus(Status.CREATE_SESSION_PLAYER_SUCCESSFUL, gameSessionPlayer);
//       }
//       else{
//           return new ResponseStatus(Status.CREATE_SESSION_PLAYER_FAIL);
//       }
//   }
//
//}
