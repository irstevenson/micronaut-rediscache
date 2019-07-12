package redis.test

import io.micronaut.cache.annotation.Cacheable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller( '/testing' )
class TestingController {
    @Cacheable( 'testing' )
    @Get( '/{id}' )
    Integer index( Integer id ) {
        id * id
    }
}
