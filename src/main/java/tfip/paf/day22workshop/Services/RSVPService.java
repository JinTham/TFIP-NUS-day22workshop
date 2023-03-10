package tfip.paf.day22workshop.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tfip.paf.day22workshop.Model.RSVP;
import tfip.paf.day22workshop.Repositories.RSVPRepository;

@Service
public class RSVPService {
    
    @Autowired
    RSVPRepository rsvpRepo;

    public List<RSVP> findAllRSVP() {
        return rsvpRepo.findAllRSVP();
    }

    public List<RSVP> findRSVPByName(String wildcard) {
        return rsvpRepo.findRSVPByName(wildcard);
    }

    public RSVP findRSVPByEmail(String email) {
        return rsvpRepo.findRSVPByEmail(email);
    }

    public Boolean insertRSVP(RSVP rsvp) {
        RSVP existingRSVP = rsvpRepo.findRSVPByEmail(rsvp.getEmail());
        if (existingRSVP == null) {
            return rsvpRepo.insertRSVP(rsvp);
        }
        return rsvpRepo.updateRSVP(rsvp);
    }

    public Boolean updateRSVP(RSVP rsvp) {
        return rsvpRepo.updateRSVP(rsvp);
    }

    public Integer count() {
        return rsvpRepo.count();
    }

}
