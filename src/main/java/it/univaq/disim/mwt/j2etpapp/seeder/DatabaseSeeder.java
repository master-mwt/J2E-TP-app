package it.univaq.disim.mwt.j2etpapp.seeder;

import com.github.javafaker.Faker;
import it.univaq.disim.mwt.j2etpapp.domain.Channel;
import it.univaq.disim.mwt.j2etpapp.domain.Post;
import it.univaq.disim.mwt.j2etpapp.domain.User;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.ChannelRepository;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.UserRepository;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

// TODO: Replace println with log
@Component
public class DatabaseSeeder {

    // Faker
    private Faker faker = new Faker();

    // Repositories
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private PostRepository postRepository;

    @EventListener
    public void seed(ContextRefreshedEvent event){
        /*System.out.println("Seedeing...");
        seedUser(10L);
        seedChannel(10L);
        seedPost(15L);
        System.out.println("End seeding.");*/
    }

    // seeders
    private void seedUser(Long iter){
        for(long i = 0; i < iter; i++){
            User user = new User();
            user.setName(faker.name().firstName());
            user.setSurname(faker.name().lastName());
            user.setEmail(faker.internet().safeEmailAddress());
            user.setPassword(faker.internet().password());

            userRepository.save(user);
        }
    }

    private void seedChannel(Long iter){
        for(long i = 0; i < iter; i++){
            Channel channel = new Channel();
            channel.setName(faker.bothify("?????######ChannelName"));
            channel.setDescription(faker.chuckNorris().fact());
            channel.setTitle(faker.internet().domainWord());

            channelRepository.save(channel);
        }
    }

    private void seedPost(Long iter){
        for(long i = 0; i < iter; i++){
            Post post = new Post();
            post.setContent(faker.lorem().sentence());
            post.setTitle(faker.lorem().word());
            post.setChannelId(randomElement(channelRepository.findAll()).getId());
            post.setUserId(randomElement(userRepository.findAll()).getId());

            postRepository.save(post);
        }
    }

    private <T> T randomElement(Iterable<T> list){
        Random random = new Random();
        return ((List<T>) list).get(random.nextInt(((List<T>) list).size()));
    }
}
