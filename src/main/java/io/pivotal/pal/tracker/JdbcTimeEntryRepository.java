package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        jdbcTemplate.update("INSERT INTO time_entries (project_id, user_id, hours, date)" + " VALUES (?,?,?,?)"
                , timeEntry.getProjectId(),timeEntry.getUserId(),timeEntry.getHours(), Date.valueOf(timeEntry.getDate()));

        int result = jdbcTemplate.queryForObject("select id from time_entries", Integer.class);
        System.out.println("*******************result***************");
        System.out.println(result);
        timeEntry.setId(result);
        return timeEntry;

    }




    @Override
    public TimeEntry find(long timeEntryId) {
        return jdbcTemplate.query(
                "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
                new Object[]{timeEntryId},
                extractor);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query(
                "SELECT id, project_id, user_id, date, hours FROM time_entries",mapper);
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {
        jdbcTemplate.update("UPDATE time_entries SET project_id = ?, user_id =?, hours =?, date=?" + " WHERE id = ?"
                , timeEntry.getProjectId(),timeEntry.getUserId(),timeEntry.getHours(), Date.valueOf(timeEntry.getDate()), timeEntryId);


        return find(timeEntryId);
    }

    @Override
    public void delete(long timeEntryId) {

        jdbcTemplate.update("DELETE FROM time_entries " + " WHERE id = ?"
                ,timeEntryId);


    }


    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
