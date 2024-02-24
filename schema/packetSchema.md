
PACKET DEFINITION

ClientPackets

ClientRequestPacket - RESPONSE ServerStatePacket
START CLIENT REQUEST END
We can then figure out required name for the packet

ClientSetPacket - RESPONSE ServerConfirmSetPacket
START CLIENT SET POSITION 6 2 6 END

ClientJoinPacket - RESPONSE ServerConfirmJoinPacket
START CLIENT JOIN END

ClientLeavePacket - RESPONSE NOTHING
START CLIENT LEAVE END


################# EXAMPLE #################
START CLIENT JOIN END
START CLIENT SET POSITION 2 2 2 END
START CLIENT REQUEST END
###########################################

ServerPackets

ServerStatePacket - RESPONDS TO ClientRequestPacket
START SERVER STATE POSITION name 1 1 1.5 END

ServerConfirmSetPacket - RESPONDS TO ClientSetPacket
START SERVER CONFIRM SET END

ServerConfirmJoinPacket - RESPONDS TO ClientJoinPacket
START SERVER CONFIRM JOIN END

TO DO:

ServerRegisterplayerPacket - IS LOADED BY ClientJoinPacket
START SERVER ADD <name> END

ServerRemoveplayerPacket - IS LOADED BY ClientLeavePacket
START SERVER REMOVE <name> END



################# EXTRA PACKETS FOR LATER ERRORS #################

ServerDenyJoinPacket
START SERVER DENYJOIN END

ServerErrorPacket
START SERVER ERROR message JOIN END

##################################################################


##################################################################
Send list of players every turn 

START SERVER ADD 3 END
START SERVER ADD 2 END
START SERVER STATE POSITION 2 1 1 1.5 END
START SERVER STATE POSITION 3 1 1 1.5 END


START SERVER PLAYERS 2 3 END
START SERVER STATE POSITION 2 1 1 1.5 END
START SERVER STATE POSITION 3 1 1 1.5 END







##################################################################



START CLIENT REQUEST END
START SERVER ADD 3 END
START SERVER ADD 2 END
START SERVER STATE POSITION 2 1 1 1.5 END
START SERVER STATE POSITION 3 1 1 1.5 END

-Ensure no packet loss - if impossible revert to sending all data
-Send postion data only when changed