package it.univaq.disim.mwt.j2etpapp.seeder;

import com.github.javafaker.Faker;
import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class DatabaseSeeder {

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
    @Autowired
    private NotificationBO notificationBO;

    @Qualifier("webApplicationContext")
    @Autowired
    private ResourceLoader resourceLoader;

    // Faker
    private Faker faker = new Faker();

    @Autowired
    private PasswordEncoder passwordEncoder;
    
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
            tagBO.count() == 0 &&
            notificationBO.count() == 0
        ){
            log.info("Empty database detected");
            doSeed();
        } else {
            log.info("Database already seeded, skipping...");
        }
    }


    private void doSeed(){
        log.info("Seeding...");

        try {
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

        } catch (BusinessException e) {
            log.info("doSeed: Error in seeding tables");
        }

        log.info("End seeding.");
    }

    // seeders
    private void seedServices() {
        log.info("Seeding services");

        ArrayList<ServiceClass> list = new ArrayList<>();

        ServiceClass mod_user_data = new ServiceClass();
        mod_user_data.setName("mod_user_data");

        ServiceClass create_post = new ServiceClass();
        create_post.setName("create_post");

        ServiceClass delete_post = new ServiceClass();
        delete_post.setName("delete_post");

        ServiceClass create_reply = new ServiceClass();
        create_reply.setName("create_reply");

        ServiceClass delete_reply = new ServiceClass();
        delete_reply.setName("delete_reply");

        ServiceClass create_channel = new ServiceClass();
        create_channel.setName("create_channel");

        ServiceClass mod_channel_data = new ServiceClass();
        mod_channel_data.setName("mod_channel_data");

        ServiceClass view_channel_members = new ServiceClass();
        view_channel_members.setName("view_channel_members");

        ServiceClass join_channel = new ServiceClass();
        join_channel.setName("join_channel");

        ServiceClass leave_channel = new ServiceClass();
        leave_channel.setName("leave_channel");

        ServiceClass delete_channel = new ServiceClass();
        delete_channel.setName("delete_channel");

        ServiceClass report_user_in_channel = new ServiceClass();
        report_user_in_channel.setName("report_user_in_channel");

        ServiceClass global_unreport_post_in_channel = new ServiceClass();
        global_unreport_post_in_channel.setName("global_unreport_post_in_channel");

        ServiceClass softban_user_in_channel = new ServiceClass();
        softban_user_in_channel.setName("softban_user_in_channel");

        ServiceClass upgrade_member_to_moderator_in_channel = new ServiceClass();
        upgrade_member_to_moderator_in_channel.setName("upgrade_member_to_moderator_in_channel");

        ServiceClass upgrade_moderator_to_admin_in_channel = new ServiceClass();
        upgrade_moderator_to_admin_in_channel.setName("upgrade_moderator_to_admin_in_channel");

        ServiceClass downgrade_moderator_to_member_in_channel = new ServiceClass();
        downgrade_moderator_to_member_in_channel.setName("downgrade_moderator_to_member_in_channel");

        ServiceClass upgrade_admin_to_creator_in_channel = new ServiceClass();
        upgrade_admin_to_creator_in_channel.setName("upgrade_admin_to_creator_in_channel");

        ServiceClass downgrade_admin_to_moderator_in_channel = new ServiceClass();
        downgrade_admin_to_moderator_in_channel.setName("downgrade_admin_to_moderator_in_channel");

        ServiceClass downgrade_creator_to_admin_in_channel = new ServiceClass();
        downgrade_creator_to_admin_in_channel.setName("downgrade_creator_to_admin_in_channel");

        ServiceClass hardban_user_from_platform = new ServiceClass();
        hardban_user_from_platform.setName("hardban_user_from_platform");

        ServiceClass upgrade_user_to_administrator = new ServiceClass();
        upgrade_user_to_administrator.setName("upgrade_user_to_administrator");

        ServiceClass downgrade_user_to_logged = new ServiceClass();
        downgrade_user_to_logged.setName("downgrade_user_to_logged");

        list.add(mod_user_data);
        list.add(create_post);
        list.add(delete_post);
        list.add(create_reply);
        list.add(delete_reply);
        list.add(create_channel);
        list.add(mod_channel_data);
        list.add(view_channel_members);
        list.add(join_channel);
        list.add(leave_channel);
        list.add(delete_channel);
        list.add(report_user_in_channel);
        list.add(global_unreport_post_in_channel);
        list.add(upgrade_member_to_moderator_in_channel);
        list.add(upgrade_moderator_to_admin_in_channel);
        list.add(downgrade_moderator_to_member_in_channel);
        list.add(upgrade_admin_to_creator_in_channel);
        list.add(downgrade_admin_to_moderator_in_channel);
        list.add(downgrade_creator_to_admin_in_channel);
        list.add(softban_user_in_channel);
        list.add(hardban_user_from_platform);
        list.add(upgrade_user_to_administrator);
        list.add(downgrade_user_to_logged);

        serviceBO.saveAll(list);
    }

    private void seedGroups() {
        log.info("Seeding groups");

        ArrayList<GroupClass> list = new ArrayList<>();

        GroupClass administrator = new GroupClass();
        administrator.setName("administrator");
        GroupClass logged = new GroupClass();
        logged.setName("logged");

        Set<ServiceClass> administratorServices = new HashSet<>(serviceBO.findAll());
        administratorServices.remove(serviceBO.findByName("join_channel"));
        administrator.setServices(administratorServices);

        Set<ServiceClass> loggedServices = new HashSet<>();
        loggedServices.add(serviceBO.findByName("create_channel"));
        loggedServices.add(serviceBO.findByName("join_channel"));
        loggedServices.add(serviceBO.findByName("mod_user_data"));

        logged.setServices(loggedServices);

        list.add(administrator);
        list.add(logged);
        groupBO.saveAll(list);
    }

    private void seedRoles() {
        log.info("Seeding roles");

        ArrayList<RoleClass> list = new ArrayList<>();

        Set<ServiceClass> creatorServices = new HashSet<>();
        creatorServices.add(serviceBO.findByName("create_post"));
        creatorServices.add(serviceBO.findByName("delete_post"));
        creatorServices.add(serviceBO.findByName("create_reply"));
        creatorServices.add(serviceBO.findByName("delete_reply"));
        creatorServices.add(serviceBO.findByName("mod_channel_data"));
        creatorServices.add(serviceBO.findByName("delete_channel"));
        creatorServices.add(serviceBO.findByName("global_unreport_post_in_channel"));
        creatorServices.add(serviceBO.findByName("report_user_in_channel"));
        creatorServices.add(serviceBO.findByName("softban_user_in_channel"));
        creatorServices.add(serviceBO.findByName("view_channel_members"));
        creatorServices.add(serviceBO.findByName("upgrade_member_to_moderator_in_channel"));
        creatorServices.add(serviceBO.findByName("upgrade_moderator_to_admin_in_channel"));
        creatorServices.add(serviceBO.findByName("downgrade_moderator_to_member_in_channel"));
        creatorServices.add(serviceBO.findByName("downgrade_admin_to_moderator_in_channel"));

        Set<ServiceClass> adminServices = new HashSet<>();
        adminServices.add(serviceBO.findByName("create_post"));
        adminServices.add(serviceBO.findByName("delete_post"));
        adminServices.add(serviceBO.findByName("create_reply"));
        adminServices.add(serviceBO.findByName("delete_reply"));
        adminServices.add(serviceBO.findByName("mod_channel_data"));
        adminServices.add(serviceBO.findByName("leave_channel"));
        adminServices.add(serviceBO.findByName("global_unreport_post_in_channel"));
        adminServices.add(serviceBO.findByName("report_user_in_channel"));
        adminServices.add(serviceBO.findByName("softban_user_in_channel"));
        adminServices.add(serviceBO.findByName("view_channel_members"));
        adminServices.add(serviceBO.findByName("upgrade_member_to_moderator_in_channel"));
        adminServices.add(serviceBO.findByName("downgrade_moderator_to_member_in_channel"));

        Set<ServiceClass> moderatorServices = new HashSet<>();
        moderatorServices.add(serviceBO.findByName("create_post"));
        moderatorServices.add(serviceBO.findByName("delete_post"));
        moderatorServices.add(serviceBO.findByName("create_reply"));
        moderatorServices.add(serviceBO.findByName("delete_reply"));
        moderatorServices.add(serviceBO.findByName("leave_channel"));
        moderatorServices.add(serviceBO.findByName("global_unreport_post_in_channel"));
        moderatorServices.add(serviceBO.findByName("report_user_in_channel"));
        moderatorServices.add(serviceBO.findByName("view_channel_members"));

        Set<ServiceClass> memberServices = new HashSet<>();
        memberServices.add(serviceBO.findByName("create_post"));
        memberServices.add(serviceBO.findByName("delete_post"));
        memberServices.add(serviceBO.findByName("create_reply"));
        memberServices.add(serviceBO.findByName("delete_reply"));
        memberServices.add(serviceBO.findByName("leave_channel"));
        memberServices.add(serviceBO.findByName("view_channel_members"));

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
        log.info("Seeding images");

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
            log.info("seedImages error", e);
        }
    }

    private void seedUsers(Long iter){
        log.info("Seeding users");

        for(long i = 0; i < iter; i++){
            UserClass user = new UserClass();
            user.setEmail(faker.internet().safeEmailAddress());
            user.setName(faker.name().firstName());
            user.setSurname(faker.name().lastName());
            user.setUsername(faker.name().username());

            user.setPassword(passwordEncoder.encode("password"));

            user.setBirthDate(faker.date().birthday());

            user.setGroup(groupBO.findByName("logged"));

            userBO.save(user);
        }
    }

    private void seedChannels(Long iter){
        log.info("Seeding channels");

        for(long i = 0; i < iter; i++){
            ChannelClass channel = new ChannelClass();
            channel.setName(faker.bothify(faker.lorem().word() + "_#####"));
            channel.setTitle(faker.internet().domainWord());
            channel.setDescription(faker.lorem().paragraph());
            channel.setRules(faker.lorem().sentence());

            UserClass creator = randomElement(userBO.findAll());
            channel.setCreator(creator);

            channelBO.save(channel);
        }
    }

    private void seedUserChannelRoleCreators(){
        log.info("Seeding channels creators");

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
        log.info("Seeding channels roles");

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

    private void seedReportedUsers(Long nUsers) throws BusinessException {
        log.info("Seeding channels reported users");

        List<UserClass> users = shuffle(userBO.findAll());
        RoleClass creator = roleBO.findByName("creator");
        long inserted = 0;

        for(UserClass user : users){
            for(UserChannelRole userChannelRole : userChannelRoleBO.findByUserId(user.getId())) {
                if(creator.equals(userChannelRole.getRole())){
                    continue;
                }

                Set<UserClass> reportedUsers = new HashSet<>();

                reportedUsers.add(user);

                channelBO.appendReportedUsers(userChannelRole.getChannel().getId(), reportedUsers);

                inserted++;
                if(inserted >= nUsers){
                    return;
                }
            }
        }
    }

    private void seedSoftBannedUsers(Long nUsers) throws BusinessException {
        log.info("Seeding channels softbanned users");

        List<UserClass> users = shuffle(userBO.findAll());
        RoleClass creator = roleBO.findByName("creator");
        long inserted = 0;

        for(UserClass user : users){
            for(UserChannelRole userChannelRole : userChannelRoleBO.findByUserId(user.getId())) {
                if(creator.equals(userChannelRole.getRole())){
                    continue;
                }

                Set<UserClass> softBannedUsers = new HashSet<>();

                softBannedUsers.add(user);

                channelBO.appendSoftBannedUsers(userChannelRole.getChannel().getId(), softBannedUsers);

                userChannelRoleBO.delete(userChannelRole);

                inserted++;
                if(inserted >= nUsers){
                    return;
                }
            }
        }
    }

    private void seedTags(Long iter){
        log.info("Seeding tags");

        for(long i = 0; i < iter; i++){
            TagClass tag = new TagClass();
            tag.setName(faker.bothify(faker.lorem().word() + "_#####"));

            tagBO.save(tag);
        }
    }

    private void seedPosts(Long iter){
        log.info("Seeding posts");

        RoleClass admin = roleBO.findByName("admin");
        RoleClass moderator = roleBO.findByName("moderator");
        RoleClass member = roleBO.findByName("member");
        ArrayList<RoleClass> roles = new ArrayList<>();
        roles.add(admin);
        roles.add(moderator);
        roles.add(member);

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
            
            // reported post probability: 1/4
            post.setReported(randomElement(Arrays.asList(false, false, true, false)));

            if(userChannelRoleBO.findByChannelIdAndUserId(post.getChannelId(), post.getUserId()) == null){
                RoleClass role = randomElement(roles);
                UserChannelRole userChannelRole = new UserChannelRole();
                UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();

                userChannelRoleFKs.setChannelId(post.getChannelId());
                userChannelRoleFKs.setRoleId(role.getId());
                userChannelRoleFKs.setUserId(post.getUserId());
                userChannelRole.setUserChannelRoleFKs(userChannelRoleFKs);

                userChannelRoleBO.save(userChannelRole);
            }

            /*Set<Long> images = new HashSet<>();
            images.add(imageBO.findByCaption("post_default.png").getId());
            post.setImages(images);*/

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

            if(post.isReported()) {
                Set<Long> usersReportedSet = new HashSet<>();
                for(UserClass user : pickRandomElements(userBO.findAll(), randomElement(Arrays.asList(1L,2L,3L,4L)))){
                    usersReportedSet.add(user.getId());
                }

                post.setUsersReported(usersReportedSet);
            }

            Set<Long> usersSavedSet = new HashSet<>();
            for(UserClass user : pickRandomElements(userBO.findAll(), 2L)){
                usersSavedSet.add(user.getId());
            }

            post.setUsersSaved(usersSavedSet);

            postBO.save(post);
        }
    }

    private void seedReplies(Long iter){
        log.info("Seeding replies");

        RoleClass admin = roleBO.findByName("admin");
        RoleClass moderator = roleBO.findByName("moderator");
        RoleClass member = roleBO.findByName("member");
        ArrayList<RoleClass> roles = new ArrayList<>();
        roles.add(admin);
        roles.add(moderator);
        roles.add(member);

        for(long i = 0; i < iter; i++){
            ReplyClass reply = new ReplyClass();

            reply.setContent(faker.lorem().sentence());
            reply.setUpvote(faker.random().nextInt(0, 3).longValue());
            reply.setDownvote(faker.random().nextInt(0, 3).longValue());

            PostClass post = randomElement(postBO.findAll());

            reply.setUserId(randomElement(userBO.findAll()).getId());
            reply.setChannelId(post.getChannelId());

            if(userChannelRoleBO.findByChannelIdAndUserId(reply.getChannelId(), reply.getUserId()) == null){
                RoleClass role = randomElement(roles);
                UserChannelRole userChannelRole = new UserChannelRole();
                UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();

                userChannelRoleFKs.setChannelId(reply.getChannelId());
                userChannelRoleFKs.setRoleId(role.getId());
                userChannelRoleFKs.setUserId(reply.getUserId());
                userChannelRole.setUserChannelRoleFKs(userChannelRoleFKs);

                userChannelRoleBO.save(userChannelRole);
            }

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
        log.info("Seeding administrators");

        UserClass user = new UserClass();
        user.setEmail("a@a.it");
        user.setName("a");
        user.setSurname("a");
        user.setUsername("a.admin");

        user.setPassword(passwordEncoder.encode("password"));

        user.setBirthDate(faker.date().birthday());

        user.setGroup(groupBO.findByName("administrator"));

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
