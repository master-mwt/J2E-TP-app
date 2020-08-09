package it.univaq.disim.mwt.j2etpapp.seeder;

import com.github.javafaker.Faker;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.*;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.PostRepository;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.ReplyRepository;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

// TODO: Replace println and printStackTrace with log
// TODO: Roles, Services, Groups rules
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
    private ImageRepository imageRepository;
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

    @Qualifier("webApplicationContext")
    @Autowired
    private ResourceLoader resourceLoader;


    @EventListener
    public void seed(ContextRefreshedEvent event){
        if( !channelRepository.findAll().iterator().hasNext() &&
            !groupRepository.findAll().iterator().hasNext() &&
            !imageRepository.findAll().iterator().hasNext() &&
            !roleRepository.findAll().iterator().hasNext() &&
            !serviceRepository.findAll().iterator().hasNext() &&
            !userChannelRoleRepository.findAll().iterator().hasNext() &&
            !userRepository.findAll().iterator().hasNext() &&
            !postRepository.findAll().iterator().hasNext() &&
            !replyRepository.findAll().iterator().hasNext() &&
            !tagRepository.findAll().iterator().hasNext()
        ){
            System.out.println("Empty database detected");
            doSeed();
        } else {
            System.out.println("Database already seeded, skipping...");
        }
    }


    private void doSeed(){
        System.out.println("Seeding...");
        // static tables
        seedServices();
        seedGroups();
        seedRoles();
        seedImages();

        // dynamic tables
        seedUsers(20L);
        seedChannels(15L);

        // relations
        seedUserChannelRoleCreators();
        seedUserChannelRoleNonCreators(8L);
        seedReportedUsers(2L);
        seedSoftBannedUsers(2L);

        // mongodb collections
        seedTags(10L);
        seedPosts(8L);
        seedReplies(5L);
        System.out.println("End seeding.");
    }

    // seeders
    private void seedServices() {
        ArrayList<ServiceClass> list = new ArrayList<>();

        ServiceClass create_post = new ServiceClass();
        create_post.setName("create_post");
        ServiceClass create_channel = new ServiceClass();
        create_channel.setName("create_channel");

        list.add(create_post);
        list.add(create_channel);
        serviceRepository.saveAll(list);
    }

    private void seedGroups() {
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

    private void seedRoles() {
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

    private void seedImages(){
        try {
            Resource[] resources = loadResources("classpath*:templates/images/*.*");
            BufferedImage img = null;

            for(Resource res : resources){
                ImageClass image = new ImageClass();
                image.setLocation("templates/images/" + res.getFilename());

                img = ImageIO.read(res.getURL());
                image.setSize(img.getWidth() + "x" + img.getHeight());
                image.setType((getExtension(res.getFilename()) != null) ? "image/" + getExtension(res.getFilename()) : "");
                image.setCaption(res.getFilename());

                imageRepository.save(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void seedUsers(Long iter){
        for(long i = 0; i < iter; i++){
            UserClass user = new UserClass();
            user.setEmail(faker.internet().safeEmailAddress());
            user.setName(faker.name().firstName());
            user.setSurname(faker.name().lastName());
            user.setUsername(faker.name().username());
            user.setPassword("password");

            user.setGroup(groupRepository.findByName("logged"));

            user.setImage(imageRepository.findByCaption("no_profile_img.jpg"));

            userRepository.save(user);
        }
    }

    private void seedChannels(Long iter){
        for(long i = 0; i < iter; i++){
            ChannelClass channel = new ChannelClass();
            channel.setName(faker.bothify(faker.lorem().word() + "_#####"));
            channel.setTitle(faker.internet().domainWord());
            channel.setDescription(faker.lorem().paragraph());
            channel.setRules(faker.lorem().sentence());

            UserClass creator = randomElement(userRepository.findAll());
            channel.setCreator(creator);

            channel.setImage(imageRepository.findByCaption("no_channel_img.jpg"));

            channelRepository.save(channel);
        }
    }

    private void seedUserChannelRoleCreators(){
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

    private void seedUserChannelRoleNonCreators(Long iter){
        RoleClass admin = roleRepository.findByName("admin");
        RoleClass moderator = roleRepository.findByName("moderator");
        RoleClass member = roleRepository.findByName("member");
        ArrayList<RoleClass> roles = new ArrayList<>();
        roles.add(admin);
        roles.add(moderator);
        roles.add(member);

        for(long i = 0; i < iter; i++){
            ChannelClass channel = randomElement(channelRepository.findAll());
            List<UserChannelRole> userChannelRoles = userChannelRoleRepository.findUserChannelRolesByChannelId(channel.getId());
            RoleClass role = randomElement(roles);

            for(UserClass user : shuffle(userRepository.findAll())){
                boolean addable = true;
                for(UserChannelRole userChannelRole : userChannelRoles){
                    if(userChannelRole.getUserChannelRoleFKs().getUserId().equals(user.getId())){
                        addable = false;
                        break;
                    }
                }
                if(addable){
                    UserChannelRole userChannelRole = new UserChannelRole();
                    UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();

                    userChannelRoleFKs.setChannelId(channel.getId());
                    userChannelRoleFKs.setRoleId(role.getId());
                    userChannelRoleFKs.setUserId(user.getId());

                    userChannelRole.setUserChannelRoleFKs(userChannelRoleFKs);
                    userChannelRole.setChannel(channel);
                    userChannelRole.setRole(role);
                    userChannelRole.setUser(user);

                    userChannelRoleRepository.save(userChannelRole);
                    break;
                }
            }
        }
    }

    private void seedReportedUsers(Long nUsers){
        List<UserClass> users = pickRandomElements(userRepository.findAll(), nUsers);
        long inserted = 0;
        for(UserClass user : users){
            for(ChannelClass channel : channelRepository.findAll()){
                if(channel.getCreator().getId().equals(user.getId())){
                    continue;
                }
                Set<UserClass> reportedUsers = null;
                if(channel.getReportedUsers() != null){
                    reportedUsers = channel.getReportedUsers();
                } else {
                    reportedUsers = new HashSet<>();
                }

                reportedUsers.add(user);
                channel.setReportedUsers(reportedUsers);

                channelRepository.save(channel);
                inserted++;
                if(inserted >= nUsers){
                    return;
                }
            }
        }
    }

    private void seedSoftBannedUsers(Long nUsers){
        List<UserClass> users = pickRandomElements(userRepository.findAll(), nUsers);
        long inserted = 0;
        for(UserClass user : users){
            for(ChannelClass channel : channelRepository.findAll()){
                if(channel.getCreator().getId().equals(user.getId())){
                    continue;
                }
                Set<UserClass> softBannedUsers = null;
                if(channel.getSoftBannedUsers() != null){
                    softBannedUsers = channel.getSoftBannedUsers();
                } else {
                    softBannedUsers = new HashSet<>();
                }

                softBannedUsers.add(user);
                channel.setSoftBannedUsers(softBannedUsers);

                channelRepository.save(channel);
                inserted++;
                if(inserted >= nUsers){
                    return;
                }
            }
        }
    }

    private void seedTags(Long iter){
        for(long i = 0; i < iter; i++){
            TagClass tag = new TagClass();
            tag.setName(faker.bothify(faker.lorem().word() + "_#####"));

            tagRepository.save(tag);
        }
    }

    private void seedPosts(Long iter){
        for(long i = 0; i < iter; i++){
            PostClass post = new PostClass();
            post.setTitle(faker.lorem().word());
            post.setContent(faker.lorem().sentence());
            post.setUpvote(faker.random().nextInt(0, 3).longValue());
            post.setDownvote(faker.random().nextInt(0, 3).longValue());
            post.setUserId(randomElement(userRepository.findAll()).getId());
            post.setChannelId(randomElement(channelRepository.findAll()).getId());

            Set<Long> images = new HashSet<>();
            images.add(imageRepository.findByCaption("post_default.png").getId());
            post.setImages(images);

            Set<TagClass> tags = new HashSet<>();
            tags.add(randomElement(tagRepository.findAll()));

            post.setTags(tags);

            Set<Long> usersDownvotedSet = new HashSet<>();
            for(UserClass user : pickRandomElements(userRepository.findAll(), post.getDownvote())){
                usersDownvotedSet.add(user.getId());
            }

            post.setUsersDownvoted(usersDownvotedSet);

            Set<Long> usersUpvotedSet = new HashSet<>();
            long k = 0;
            while(k < post.getUpvote()){
                Long userId = randomElement(userRepository.findAll()).getId();
                if(usersDownvotedSet.contains(userId)){
                    continue;
                }
                usersUpvotedSet.add(userId);
                k++;
            }

            post.setUsersUpvoted(usersUpvotedSet);

            Set<Long> usersHiddenSet = new HashSet<>();
            for(UserClass user : pickRandomElements(userRepository.findAll(), 2L)){
                usersHiddenSet.add(user.getId());
            }

            post.setUsersHidden(usersHiddenSet);

            Set<Long> usersReportedSet = new HashSet<>();
            for(UserClass user : pickRandomElements(userRepository.findAll(), 2L)){
                usersReportedSet.add(user.getId());
            }

            post.setUsersReported(usersReportedSet);

            Set<Long> usersSavedSet = new HashSet<>();
            for(UserClass user : pickRandomElements(userRepository.findAll(), 2L)){
                usersSavedSet.add(user.getId());
            }

            post.setUsersSaved(usersSavedSet);

            postRepository.save(post);
        }
    }

    private void seedReplies(Long iter){
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
            long k = 0;
            while(k < reply.getUpvote()){
                Long userId = randomElement(userRepository.findAll()).getId();
                if(usersDownvotedSet.contains(userId)){
                    continue;
                }
                usersUpvotedSet.add(userId);
                k++;
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

    private <T> List<T> shuffle(Iterable<T> list){
        ArrayList<T> resultList = new ArrayList<T>();
        for (T element : list){
            resultList.add(element);
        }
        Collections.shuffle(resultList);
        return resultList;
    }

    private Resource[] loadResources(String pattern) throws IOException {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
    }

    private String getExtension(String filename){
        String extension = null;
        int i = filename.lastIndexOf('.');
        if(i > 0){
            extension = filename.substring(i+1);
        }

        return extension;
    }
}
