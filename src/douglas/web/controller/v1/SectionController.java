package douglas.web.controller.v1;

import douglas.domain.Section;
import douglas.domain.Test;
import douglas.persistence.SectionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest/v1/sections")
public class SectionController {

    private SectionDao sectionDao;

    @Autowired
    public SectionController(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Transactional
    @RequestMapping(path = {"/byProductId/{productId}"}, method = RequestMethod.GET)
    public List<Section> getSectionsByProductId(@PathVariable String productId) {
        return sectionDao.allSectionsByProductId(productId);
    }

    @Transactional
    @RequestMapping(path = {"/run/{sectionId}"}, method = RequestMethod.GET)
    public void runTestBySectionId(@PathVariable String sectionId) {
        sectionDao.run(sectionId);
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.POST)
    public void createSection(@RequestBody Section section) {
        sectionDao.save(section);
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.PUT)
    public void updateSection(@RequestBody Section section) {
        sectionDao.update(section);
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.DELETE)
    public void deleteSection(@RequestBody Section section) {
        sectionDao.delete(section);
    }

}