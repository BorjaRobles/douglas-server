package douglas.web.controller.v1;

import douglas.domain.Product;
import douglas.persistence.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest/v1/products")
public class ProductController {

    private ProductDao productDao;

    @Autowired
    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    @RequestMapping(path = {"/{productId}"}, method = RequestMethod.GET)
    public Product findProduct(@PathVariable String productId) {
        return productDao.findById(productId);
    }

    @Transactional
    @RequestMapping(path = {"/run/{productId}"}, method = RequestMethod.GET)
    public void runTestByProductId(@PathVariable String productId) {
        productDao.run(productId);
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.GET)
    public List<Product> all() {
        return productDao.all();
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.POST)
    public void createProduct(@RequestBody Product product) {
        productDao.save(product);
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.PUT)
    public void updateProduct(@RequestBody Product product) {
        productDao.update(product);
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.DELETE)
    public void deleteProduct(@RequestBody Product product) {
        productDao.delete(product);
    }

}