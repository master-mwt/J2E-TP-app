package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.configuration.ApplicationProperties;
import it.univaq.disim.mwt.j2etpapp.domain.ImageClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.ImageRepository;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.UserRepository;
import it.univaq.disim.mwt.j2etpapp.utils.FileDealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class UserBOImpl implements UserBO {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private FileDealer fileDealer;
    @Autowired
    private ApplicationProperties properties;

    private PasswordEncoder passwordEncoder;

    // TODO: is this ok ?
    @Autowired
    private void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserClass> findAll() {
        return (List<UserClass>) userRepository.findAll();
    }

    @Override
    public UserClass findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserClass findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public List<UserClass> findByUsernameContains(String username) {
        return userRepository.findByUsernameContains(username).orElse(new ArrayList<>());
    }

    @Override
    public Page<UserClass> findByUsernameContainsPaginated(String username, int page, int size) {
        return new Page<>(userRepository.findByUsernameContains(username, PageRequest.of(page, size)));
    }

    @Override
    public List<UserClass> findByEmailContains(String email) {
        return userRepository.findByEmailContains(email).orElse(new ArrayList<>());
    }

    @Override
    public Page<UserClass> findByEmailContainsPaginated(String email, int page, int size) {
        return new Page<>(userRepository.findByEmailContains(email, PageRequest.of(page, size)));
    }

    @Override
    public void save(UserClass user) {
        userRepository.save(user);
    }

    @Override
    public void saveAll(List<UserClass> users) {
        userRepository.saveAll(users);
    }

    @Override
    public void saveAll(UserClass... users) {
        userRepository.saveAll(Arrays.asList(users));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void delete(UserClass user) {
        userRepository.delete(user);
    }

    @Override
    public Long count() {
        return userRepository.count();
    }
    
    @Override
    public void hardBanToggle(Long userId) {
        UserClass user = userRepository.findById(userId).orElse(null);
        if(user.isHard_ban()){
            user.setHard_ban(false);
        } else {
            user.setHard_ban(true);
        }
        userRepository.save(user);
    }

    @Override
    public boolean checkOldPassword(UserClass user, String oldPassword) {
        if(passwordEncoder.matches(oldPassword, user.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public void changePassword(UserClass user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void removeImage(long userId) {
        UserClass user = userRepository.findById(userId).orElse(null);
        user.setImage(null);

        userRepository.save(user);
    }

    @Override
    public void saveImage(long userId, MultipartFile image) throws BusinessException {
        UserClass user = userRepository.findById(userId).orElse(null);

        try {
            String filename = fileDealer.uploadFile(image);
            ImageClass imageClass = new ImageClass();
            imageClass.setLocation(properties.getImagesStoragePathRelative() + filename);
            imageClass.setType(image.getContentType());

            BufferedImage bimg = ImageIO.read(new File(properties.getImagesStoragePathAbsolute() + filename));
            imageClass.setSize(bimg.getWidth() + "x" + bimg.getHeight());

            imageRepository.save(imageClass);
            user.setImage(imageClass);
            userRepository.save(user);

        } catch (IOException e) {
            throw new BusinessException("saveImage", e);
        }
    }

    @Override
    public void updateUserProfile(UserClass user, UserClass newData) {
        user.setName(newData.getName());
        user.setSurname(newData.getSurname());
        user.setEmail(newData.getEmail());
        user.setBirthDate(newData.getBirthDate());

        userRepository.save(user);
    }
}
