package tfip.paf.day22workshop.Repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tfip.paf.day22workshop.Model.RSVP;

@Repository
public class RSVPRepository {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String SQLselectAll = "select * from rsvp";
    private final String SQLselectByName = "select * from rsvp where name like ?";
    private final String SQLselectByEmail = "select * from rsvp where email like ?";
    private final String SQLinsert = "insert into rsvp (name,email,phone,confirmation_date,comments) values (?,?,?,?,?)";
    private final String SQLupdate = "update rsvp set name=?,phone=?,confirmation_date=?,comments=? where email = ?";
    private final String SQLcount = "select count(*) from rsvp";


    public List<RSVP> findAllRSVP() {
        return jdbcTemplate.query(SQLselectAll,BeanPropertyRowMapper.newInstance(RSVP.class));
    }

    public List<RSVP> findRSVPByName(String wildcard) {
        return jdbcTemplate.query(SQLselectByName,BeanPropertyRowMapper.newInstance(RSVP.class), wildcard);
    }

    public Boolean insertRSVP(RSVP rsvp) {
        Integer inserted = jdbcTemplate.update(SQLinsert,BeanPropertyRowMapper.newInstance(RSVP.class),
            rsvp.getName(), 
            rsvp.getEmail(),
            rsvp.getPhone(),
            rsvp.getConfirmationDate(), 
            rsvp.getComments());
        return inserted > 0;
    }

    public RSVP findRSVPByEmail(String email) {
        return jdbcTemplate.queryForObject(SQLselectByEmail,BeanPropertyRowMapper.newInstance(RSVP.class), email);
    }

    public Boolean updateRSVP(RSVP rsvp) {
        RSVP existingRSVP = findRSVPByEmail(rsvp.getEmail());
        Integer updated = jdbcTemplate.update(SQLupdate,BeanPropertyRowMapper.newInstance(RSVP.class),
            rsvp.getName(), 
            rsvp.getPhone(),
            rsvp.getConfirmationDate(), 
            rsvp.getComments(),
            existingRSVP.getEmail());
        return updated > 0;
    }

    public Integer count() {
        Integer count = jdbcTemplate.queryForObject(SQLcount, Integer.class);
        if (count == null) {
            return 0;
        }
        return count;
    }

    public int[] batchUpdate(List<RSVP> rsvps) {
        return jdbcTemplate.batchUpdate(SQLinsert, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1,rsvps.get(i).getName());
                ps.setString(2,rsvps.get(i).getEmail());
                ps.setString(3,rsvps.get(i).getPhone());
                ps.setDate(4,rsvps.get(i).getConfirmationDate());
                ps.setString(5,rsvps.get(i).getComments());
            }
            public int getBatchSize() {
                return rsvps.size();
            }
        });
    }
}
