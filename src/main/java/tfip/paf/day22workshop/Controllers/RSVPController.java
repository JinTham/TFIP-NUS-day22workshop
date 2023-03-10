package tfip.paf.day22workshop.Controllers;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tfip.paf.day22workshop.Exception.ResourceNotFoundException;
import tfip.paf.day22workshop.Model.RSVP;
import tfip.paf.day22workshop.Services.RSVPService;

@RestController
@RequestMapping(path="api/rsvps")
public class RSVPController {
    
    @Autowired
    RSVPService rsvpSvc;

    @GetMapping
    public ResponseEntity<List<RSVP>> getAllRSVPs() {
        List<RSVP> rsvps = rsvpSvc.findAllRSVP();
        if (rsvps.isEmpty()) {
            throw new ResourceNotFoundException("No RSVP at all");
        }
        return ResponseEntity.ok().body(rsvps);
    }

    @GetMapping
    public ResponseEntity<List<RSVP>> getRSVPByName(@RequestParam String wildcard) {
        List<RSVP> rsvps = rsvpSvc.findRSVPByName(wildcard);
        if (rsvps.isEmpty()) {
            throw new ResourceNotFoundException("No RSVP with this name");
        }
        return ResponseEntity.ok().body(rsvps);
    }

    @PostMapping(consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> createRSVP(@RequestParam MultiValueMap<String,String> form) {
        RSVP rsvp = new RSVP();
        rsvp.setEmail(form.getFirst("email"));
        rsvp.setName(form.getFirst("name"));
        rsvp.setPhone(form.getFirst("phone"));
        rsvp.setConfirmationDate((Date) form.get("confimrationDate"));
        rsvp.setComments(form.getFirst("comments"));
        Boolean created = rsvpSvc.insertRSVP(rsvp);
        if (created) {
            return ResponseEntity.ok().body("New entry created");
        }
        return ResponseEntity.ok().body("New entry not created");
    }

    @PutMapping(path="/{email}",consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> updateRSVP(@RequestParam MultiValueMap<String,String> form, @PathVariable("email") String email) {
        RSVP rsvp = rsvpSvc.findRSVPByEmail(email);
        rsvp.setName(form.getFirst("name"));
        rsvp.setPhone(form.getFirst("phone"));
        rsvp.setConfirmationDate((Date) form.get("confimrationDate"));
        rsvp.setComments(form.getFirst("comments"));
        Boolean updated = rsvpSvc.updateRSVP(rsvp);
        if (updated) {
            return ResponseEntity.ok().body("Entry updated");
        }
        return ResponseEntity.ok().body("Entry not updated");
    }

    @GetMapping(path="/count")
    public ResponseEntity<Integer> getRSVPCount() {
        return new ResponseEntity<Integer>(rsvpSvc.count(),HttpStatus.OK);
    }

}
