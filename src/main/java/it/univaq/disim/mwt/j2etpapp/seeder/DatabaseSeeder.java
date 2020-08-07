package it.univaq.disim.mwt.j2etpapp.seeder;

import com.github.javafaker.Faker;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.*;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.PostRepository;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.ReplyRepository;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

// TODO: Replace println with log
// TODO: UserChannelRole non-creators seeding and other tables
@Component
@Transactional
public class DatabaseSeeder {

    // Faker
    private Faker faker = new Faker();

    // Repositories
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private UserChannelRoleRepository userChannelRoleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private TagRepository tagRepository;


    @EventListener
    public void seeder(ContextRefreshedEvent event){
        // TODO: control only on service table (maybe is better doing it for all tables in OR ?)
        if(!serviceRepository.findAll().iterator().hasNext()){
            System.out.println("Empty database detected");
            seed();
        } else {
            System.out.println("Database already seeded, skipping...");
        }
    }


    private void seed(){
        System.out.println("Seeding...");
        seedService();
        seedGroup();
        seedRole();

        seedUser(15L);
        seedChannel(15L);

        // TODO: non-creators seeding
        seedUserChannelRole();

        seedTag(10L);
        seedPost(8L);
        seedReply(5L);
        System.out.println("End seeding.");
    }

    // seeders
    private void seedService() {
        ArrayList<ServiceClass> list = new ArrayList<>();

        ServiceClass create_post = new ServiceClass();
        create_post.setName("create_post");
        ServiceClass create_channel = new ServiceClass();
        create_channel.setName("create_channel");

        list.add(create_post);
        list.add(create_channel);
        serviceRepository.saveAll(list);
    }

    private void seedGroup() {
        ArrayList<GroupClass> list = new ArrayList<>();

        GroupClass administrator = new GroupClass();
        administrator.setName("administrator");
        GroupClass logged = new GroupClass();
        logged.setName("logged");

        Set<ServiceClass> administratorServices = new HashSet<>();
        for(ServiceClass service : serviceRepository.findAll()){
            administratorServices.add(service);
        }
        administrator.setServices(administratorServices);

        Set<ServiceClass> loggedServices = new HashSet<>();
        loggedServices.add(serviceRepository.findByName("create_post"));
        loggedServices.add(serviceRepository.findByName("create_channel"));
        logged.setServices(loggedServices);

        list.add(administrator);
        list.add(logged);
        groupRepository.saveAll(list);
    }

    private void seedRole() {
        ArrayList<RoleClass> list = new ArrayList<>();

        Set<ServiceClass> creatorServices = new HashSet<>();
        creatorServices.add(serviceRepository.findByName("create_post"));
        creatorServices.add(serviceRepository.findByName("create_channel"));

        Set<ServiceClass> adminServices = new HashSet<>();
        adminServices.add(serviceRepository.findByName("create_post"));
        adminServices.add(serviceRepository.findByName("create_channel"));

        Set<ServiceClass> moderatorServices = new HashSet<>();
        moderatorServices.add(serviceRepository.findByName("create_post"));
        moderatorServices.add(serviceRepository.findByName("create_channel"));

        Set<ServiceClass> memberServices = new HashSet<>();
        memberServices.add(serviceRepository.findByName("create_post"));
        memberServices.add(serviceRepository.findByName("create_channel"));

        RoleClass creator = new RoleClass();
        creator.setName("creator");
        creator.setServices(creatorServices);

        RoleClass admin = new RoleClass();
        admin.setName("admin");
        admin.setServices(adminServices);

        RoleClass moderator = new RoleClass();
        moderator.setName("moderator");
        moderator.setServices(moderatorServices);

        RoleClass member = new RoleClass();
        member.setName("member");
        member.setServices(memberServices);


        list.add(creator);
        list.add(admin);
        list.add(moderator);
        list.add(member);
        roleRepository.saveAll(list);
    }

    private void seedUser(Long iter){
        for(long i = 0; i < iter; i++){
            UserClass user = new UserClass();
            user.setEmail(faker.internet().safeEmailAddress());
            user.setName(faker.name().firstName());
            user.setSurname(faker.name().lastName());
            user.setUsername(faker.name().username());
            user.setPassword(faker.internet().password());

            user.setGroup(groupRepository.findByName("logged"));

            userRepository.save(user);
        }
    }

    private void seedChannel(Long iter){
        for(long i = 0; i < iter; i++){
            ChannelClass channel = new ChannelClass();
            channel.setName(faker.bothify("?????######ChannelName"));
            channel.setTitle(faker.internet().domainWord());
            channel.setDescription(faker.chuckNorris().fact());
            channel.setRules(faker.lorem().sentence());

            UserClass creator = randomElement(userRepository.findAll());
            channel.setCreator(creator);

            channelRepository.save(channel);
        }
    }

    private void seedUserChannelRole(){
        RoleClass creator = roleRepository.findByName("creator");
        for(ChannelClass channel : channelRepository.findAll()){

            UserChannelRole userChannelRole = new UserChannelRole();
            UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();

            userChannelRoleFKs.setChannelId(channel.getId());
            userChannelRoleFKs.setRoleId(creator.getId());
            userChannelRoleFKs.setUserId(channel.getCreator().getId());

            userChannelRole.setUserChannelRoleFKs(userChannelRoleFKs);
            userChannelRole.setChannel(channel);
            userChannelRole.setRole(creator);
            userChannelRole.setUser(channel.getCreator());

            userChannelRoleRepository.save(userChannelRole);
        }
    }

    private void seedTag(Long iter){
        for(long i = 0; i < iter; i++){
            TagClass tag = new TagClass();
            tag.setName(faker.bothify("???###Tag"));

            tagRepository.save(tag);
        }
    }

    private void seedPost(Long iter){
        for(long i = 0; i < iter; i++){
            PostClass post = new PostClass();
            post.setTitle(faker.lorem().word());
            post.setContent(faker.lorem().sentence());
            post.setUpvote(faker.random().nextInt(0, 3).longValue());
            post.setDownvote(faker.random().nextInt(0, 3).longValue());
            post.setUserId(randomElement(userRepository.findAll()).getId());
            post.setChannelId(randomElement(channelRepository.findAll()).getId());

            Set<TagClass> tags = new HashSet<>();
            tags.add(randomElement(tagRepository.findAll()));

            post.setTags(tags);

            Set<Long> usersDownvotedSet = new HashSet<>();
            for(UserClass user : pickRandomElements(userRepository.findAll(), post.getDownvote())){
                usersDownvotedSet.add(user.getId());
            }

            post.setUsersDownvoted(usersDownvotedSet);

            Set<Long> usersUpvotedSet = new HashSet<>();
            for(long k = 0; k < post.getUpvote(); k++){
                Long userId = randomElement(userRepository.findAll()).getId();
                if(usersDownvotedSet.contains(userId)){
                    continue;
                }
                usersUpvotedSet.add(userId);
            }

            post.setUsersUpvoted(usersUpvotedSet);

            postRepository.save(post);
        }
    }

    private void seedReply(Long iter){
        for(long i = 0; i < iter; i++){
            ReplyClass reply = new ReplyClass();

            reply.setContent(faker.lorem().sentence());
            reply.setUpvote(faker.random().nextInt(0, 3).longValue());
            reply.setDownvote(faker.random().nextInt(0, 3).longValue());

            Set<Long> usersDownvotedSet = new HashSet<>();
            for(UserClass user : pickRandomElements(userRepository.findAll(), reply.getDownvote())){
                usersDownvotedSet.add(user.getId());
            }

            reply.setUsersDownvoted(usersDownvotedSet);

            Set<Long> usersUpvotedSet = new HashSet<>();
            for(long k = 0; k < reply.getUpvote(); k++){
                Long userId = randomElement(userRepository.findAll()).getId();
                if(usersDownvotedSet.contains(userId)){
                    continue;
                }
                usersUpvotedSet.add(userId);
            }

            reply.setUsersUpvoted(usersUpvotedSet);

            PostClass post = randomElement(postRepository.findAll());
            Set<ReplyClass> replies = post.getReplies();

            if(replies == null || replies.isEmpty()){
                replies = new HashSet<>();
            }

            replies.add(reply);
            post.setReplies(replies);
            reply.setPost(post);

            replyRepository.save(reply);
            postRepository.save(post);
        }
    }

    private <T> T randomElement(Iterable<T> list){
        Random random = new Random();
        return ((List<T>) list).get(random.nextInt(((List<T>) list).size()));
    }

    private <T> List<T> pickRandomElements(Iterable<T> list, Long nElements){
        ArrayList<T> resultList = new ArrayList<T>();
        for (T element : list){
            resultList.add(element);
        }
        Collections.shuffle(resultList);
        return resultList.subList(0, nElements.intValue());
    }
}
