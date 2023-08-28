package rs.ac.uns.ftn.svtkvtproject.service;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Banned;
import rs.ac.uns.ftn.svtkvtproject.model.dto.BannedDTO;

import java.util.List;

public interface BannedService {

    Banned findById(Long id);

    List<Banned> findAll();

    Banned createBanned(BannedDTO bannedDTO);

    Integer deleteBanned(Long id);

    Banned saveBanned(Banned banned);

    void  unblockUserAndSaveBanned(Long bannedId);
}
