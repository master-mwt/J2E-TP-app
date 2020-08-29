package it.univaq.disim.mwt.j2etpapp.seeder;

import com.github.javafaker.Faker;
import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;
import net.steppschuh.markdowngenerator.text.emphasis.ItalicText;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

// TODO: Replace println and printStackTrace with log
// TODO: Roles, Services, Groups rules
@Component
public class DatabaseSeeder {

    // Faker
    private Faker faker = new Faker();
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Business classes
    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private GroupBO groupBO;
    @Autowired
    private ImageBO imageBO;
    @Autowired
    private RoleBO roleBO;
    @Autowired
    private ServiceBO serviceBO;
    @Autowired
    private UserChannelRoleBO userChannelRoleBO;
    @Autowired
    private UserBO userBO;
    @Autowired
    private PostBO postBO;
    @Autowired
    private ReplyBO replyBO;
    @Autowired
    private TagBO tagBO;

    @Qualifier("webApplicationContext")
    @Autowired
    private ResourceLoader resourceLoader;


    @EventListener
    public void seed(ContextRefreshedEvent event){
        if( channelBO.count() == 0 &&
            groupBO.count() == 0 &&
            imageBO.count() == 0 &&
            roleBO.count() == 0 &&
            serviceBO.count() == 0 &&
            userChannelRoleBO.count() == 0 &&
            userBO.count() == 0 &&
            postBO.count() == 0 &&
            replyBO.count() == 0 &&
            tagBO.count() == 0
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
        seedReportedUsers(5L);
        seedSoftBannedUsers(3L);

        // mongodb collections
        seedTags(20L);
        seedPosts(40L);
        seedReplies(20L);

        // generate admins
        seedAdmins();
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
        serviceBO.saveAll(list);
    }

    private void seedGroups() {
        ArrayList<GroupClass> list = new ArrayList<>();

        GroupClass administrator = new GroupClass();
        administrator.setName("administrator");
        GroupClass logged = new GroupClass();
        logged.setName("logged");

        Set<ServiceClass> administratorServices = new HashSet<>(serviceBO.findAll());
        administrator.setServices(administratorServices);

        Set<ServiceClass> loggedServices = new HashSet<>();
        loggedServices.add(serviceBO.findByName("create_post"));
        loggedServices.add(serviceBO.findByName("create_channel"));
        logged.setServices(loggedServices);

        list.add(administrator);
        list.add(logged);
        groupBO.saveAll(list);
    }

    private void seedRoles() {
        ArrayList<RoleClass> list = new ArrayList<>();

        Set<ServiceClass> creatorServices = new HashSet<>();
        creatorServices.add(serviceBO.findByName("create_post"));
        creatorServices.add(serviceBO.findByName("create_channel"));

        Set<ServiceClass> adminServices = new HashSet<>();
        adminServices.add(serviceBO.findByName("create_post"));
        adminServices.add(serviceBO.findByName("create_channel"));

        Set<ServiceClass> moderatorServices = new HashSet<>();
        moderatorServices.add(serviceBO.findByName("create_post"));
        moderatorServices.add(serviceBO.findByName("create_channel"));

        Set<ServiceClass> memberServices = new HashSet<>();
        memberServices.add(serviceBO.findByName("create_post"));
        memberServices.add(serviceBO.findByName("create_channel"));

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
        roleBO.saveAll(list);
    }

    private void seedImages(){
        try {
            Resource[] resources = loadResources("classpath*:static/images/*.*");
            BufferedImage img = null;

            for(Resource res : resources){
                ImageClass image = new ImageClass();
                image.setLocation("images/" + res.getFilename());

                img = ImageIO.read(res.getURL());
                image.setSize(img.getWidth() + "x" + img.getHeight());
                image.setType((getExtension(res.getFilename()) != null) ? "image/" + getExtension(res.getFilename()) : "");
                image.setCaption(res.getFilename());

                imageBO.save(image);
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

            user.setPassword(passwordEncoder.encode("password"));

            user.setGroup(groupBO.findByName("logged"));

            user.setImage(imageBO.findByCaption("no_profile_img.jpg"));

            userBO.save(user);
        }
    }

    private void seedChannels(Long iter){
        for(long i = 0; i < iter; i++){
            ChannelClass channel = new ChannelClass();
            channel.setName(faker.bothify(faker.lorem().word() + "_#####"));
            channel.setTitle(faker.internet().domainWord());
            channel.setDescription(faker.lorem().paragraph());
            channel.setRules(faker.lorem().sentence());

            UserClass creator = randomElement(userBO.findAll());
            channel.setCreator(creator);

            channel.setImage(imageBO.findByCaption("no_channel_img.jpg"));

            channelBO.save(channel);
        }
    }

    private void seedUserChannelRoleCreators(){
        RoleClass creator = roleBO.findByName("creator");
        for(ChannelClass channel : channelBO.findAll()){

            UserChannelRole userChannelRole = new UserChannelRole();
            UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();

            userChannelRoleFKs.setChannelId(channel.getId());
            userChannelRoleFKs.setRoleId(creator.getId());
            userChannelRoleFKs.setUserId(channel.getCreator().getId());

            userChannelRole.setUserChannelRoleFKs(userChannelRoleFKs);
            userChannelRole.setChannel(channel);
            userChannelRole.setRole(creator);
            userChannelRole.setUser(channel.getCreator());

            userChannelRoleBO.save(userChannelRole);
        }
    }

    private void seedUserChannelRoleNonCreators(Long iter){
        RoleClass admin = roleBO.findByName("admin");
        RoleClass moderator = roleBO.findByName("moderator");
        RoleClass member = roleBO.findByName("member");
        ArrayList<RoleClass> roles = new ArrayList<>();
        roles.add(admin);
        roles.add(moderator);
        roles.add(member);

        for(long i = 0; i < iter; i++){
            ChannelClass channel = randomElement(channelBO.findAll());
            List<UserChannelRole> userChannelRoles = userChannelRoleBO.findByChannelId(channel.getId());
            RoleClass role = randomElement(roles);

            for(UserClass user : shuffle(userBO.findAll())){
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

                    userChannelRoleBO.save(userChannelRole);
                    break;
                }
            }
        }
    }

    private void seedReportedUsers(Long nUsers){
        List<UserClass> users = pickRandomElements(userBO.findAll(), nUsers);
        long inserted = 0;
        for(UserClass user : users){
            for(ChannelClass channel : channelBO.findAll()){
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

                channelBO.save(channel);
                inserted++;
                if(inserted >= nUsers){
                    return;
                }
            }
        }
    }

    private void seedSoftBannedUsers(Long nUsers){
        List<UserClass> users = pickRandomElements(userBO.findAll(), nUsers);
        long inserted = 0;
        for(UserClass user : users){
            for(ChannelClass channel : channelBO.findAll()){
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

                channelBO.save(channel);
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

            tagBO.save(tag);
        }
    }

    private void seedPosts(Long iter){
        for(long i = 0; i < iter; i++){
            PostClass post = new PostClass();
            post.setTitle(faker.lorem().word());

            StringBuilder builder = new StringBuilder();
            builder.append(new Heading(faker.lorem().sentence(), 1))
                    .append("\n")
                    .append(new Heading(faker.lorem().sentence(), 2))
                    .append("\n")
                    .append(new BoldText(faker.lorem().sentence()))
                    .append("\n")
                    .append(new ItalicText(faker.lorem().sentence()))
                    .append("\n");

            post.setContent(builder.toString());

            post.setUpvote(faker.random().nextInt(0, 3).longValue());
            post.setDownvote(faker.random().nextInt(0, 3).longValue());
            post.setUserId(randomElement(userBO.findAll()).getId());
            post.setChannelId(randomElement(channelBO.findAll()).getId());

            Set<Long> images = new HashSet<>();
            images.add(imageBO.findByCaption("post_default.png").getId());
            post.setImages(images);

            Set<TagClass> tags = new HashSet<>(pickRandomElements(tagBO.findAll(), faker.random().nextInt(0, 3).longValue()));
            post.setTags(tags);

            Set<Long> usersDownvotedSet = new HashSet<>();
            for(UserClass user : pickRandomElements(userBO.findAll(), post.getDownvote())){
                usersDownvotedSet.add(user.getId());
            }

            post.setUsersDownvoted(usersDownvotedSet);

            Set<Long> usersUpvotedSet = new HashSet<>();
            long k = 0;
            while(k < post.getUpvote()){
                Long userId = randomElement(userBO.findAll()).getId();
                if(usersDownvotedSet.contains(userId) || usersUpvotedSet.contains(userId)){
                    continue;
                }
                usersUpvotedSet.add(userId);
                k++;
            }

            post.setUsersUpvoted(usersUpvotedSet);

            Set<Long> usersHiddenSet = new HashSet<>();
            for(UserClass user : pickRandomElements(userBO.findAll(), 2L)){
                usersHiddenSet.add(user.getId());
            }

            post.setUsersHidden(usersHiddenSet);

            Set<Long> usersReportedSet = new HashSet<>();
            for(UserClass user : pickRandomElements(userBO.findAll(), 2L)){
                usersReportedSet.add(user.getId());
            }

            post.setUsersReported(usersReportedSet);

            Set<Long> usersSavedSet = new HashSet<>();
            for(UserClass user : pickRandomElements(userBO.findAll(), 2L)){
                usersSavedSet.add(user.getId());
            }

            post.setUsersSaved(usersSavedSet);

            postBO.save(post);
        }
    }

    private void seedReplies(Long iter){
        for(long i = 0; i < iter; i++){
            ReplyClass reply = new ReplyClass();

            reply.setContent(faker.lorem().sentence());
            reply.setUpvote(faker.random().nextInt(0, 3).longValue());
            reply.setDownvote(faker.random().nextInt(0, 3).longValue());

            Set<Long> usersDownvotedSet = new HashSet<>();
            for(UserClass user : pickRandomElements(userBO.findAll(), reply.getDownvote())){
                usersDownvotedSet.add(user.getId());
            }

            reply.setUsersDownvoted(usersDownvotedSet);

            Set<Long> usersUpvotedSet = new HashSet<>();
            long k = 0;
            while(k < reply.getUpvote()){
                Long userId = randomElement(userBO.findAll()).getId();
                if(usersDownvotedSet.contains(userId) || usersUpvotedSet.contains(userId)){
                    continue;
                }
                usersUpvotedSet.add(userId);
                k++;
            }

            reply.setUsersUpvoted(usersUpvotedSet);

            PostClass post = randomElement(postBO.findAll());
            Set<ReplyClass> replies = post.getReplies();

            if(replies == null || replies.isEmpty()){
                replies = new HashSet<>();
            }

            replies.add(reply);
            post.setReplies(replies);
            reply.setPost(post);

            replyBO.save(reply);
            postBO.save(post);
        }
    }

    private void seedAdmins(){
        UserClass user = new UserClass();
        user.setEmail("a@a.it");
        user.setName("a");
        user.setSurname("a");
        user.setUsername("a.admin");

        user.setPassword(passwordEncoder.encode("password"));

        user.setGroup(groupBO.findByName("administrator"));

        user.setImage(imageBO.findByCaption("no_profile_img.jpg"));

        userBO.save(user);
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
