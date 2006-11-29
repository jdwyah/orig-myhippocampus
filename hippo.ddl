
    alter table Topics 
        drop 
        foreign key FK95A7C54424CD75BC;

    alter table Topics 
        drop 
        foreign key FK95A7C544BFBBD543;

    alter table Topics 
        drop 
        foreign key FK95A7C544D7A8CDDA;

    alter table instancetable 
        drop 
        foreign key FKAEB83C59241F80B6;

    alter table instancetable 
        drop 
        foreign key FKAEB83C59E7D2111;

    alter table member_topics 
        drop 
        foreign key FKAF560A696E60CBA9;

    alter table member_topics 
        drop 
        foreign key FKAF560A69978CE7C6;

    alter table mind_tree 
        drop 
        foreign key FK2781EB6B4E230C15;

    alter table mind_tree_elements 
        drop 
        foreign key FK440EAF4B1484FB6A;

    alter table mind_tree_elements 
        drop 
        foreign key FK440EAF4BD40E47B5;

    alter table mind_tree_elements 
        drop 
        foreign key FK440EAF4B4E230C15;

    alter table occurrences 
        drop 
        foreign key FK2EC250C2B830444;

    alter table occurrences 
        drop 
        foreign key FK2EC250C2BFBBD543;

    alter table topic_associations 
        drop 
        foreign key FKD04875225C01F87F;

    alter table topic_associations 
        drop 
        foreign key FKD048752227AACB23;

    alter table topic_occurences 
        drop 
        foreign key FK6F7A6BACCEFD292F;

    alter table topic_occurences 
        drop 
        foreign key FK6F7A6BAC479BD4A5;

    alter table topic_scopes 
        drop 
        foreign key FK534459AF2A7FE1CC;

    alter table topic_scopes 
        drop 
        foreign key FK534459AFE7D2111;

    alter table typetable 
        drop 
        foreign key FK201420D4241F80B6;

    alter table typetable 
        drop 
        foreign key FK201420D4E7D2111;

    drop table if exists Topics;

    drop table if exists Users;

    drop table if exists instancetable;

    drop table if exists member_topics;

    drop table if exists mind_tree;

    drop table if exists mind_tree_elements;

    drop table if exists occurrences;

    drop table if exists subjects;

    drop table if exists topic_associations;

    drop table if exists topic_occurences;

    drop table if exists topic_scopes;

    drop table if exists typetable;

    create table Topics (
        topic_id bigint not null auto_increment,
        discriminator varchar(255) not null,
        user_id bigint,
        title varchar(255),
        latitude integer not null,
        longitude integer not null,
        dateUpdated timestamp,
        dateCreated timestamp,
        public_visible bit,
        subject bigint,
        subject_id bigint,
        primary key (topic_id)
    ) type=InnoDB;

    create table Users (
        user_id bigint not null auto_increment,
        user_name varchar(255),
        password varchar(255),
        enabled bit,
        supervisor bit,
        primary key (user_id)
    ) type=InnoDB;

    create table instancetable (
        from_id bigint not null,
        topic_id bigint not null,
        primary key (from_id, topic_id)
    ) type=InnoDB;

    create table member_topics (
        member_topics_id bigint not null,
        member_id bigint not null,
        primary key (member_topics_id, member_id)
    ) type=InnoDB;

    create table mind_tree (
        map_id bigint not null auto_increment,
        topic bigint,
        primary key (map_id)
    ) type=InnoDB;

    create table mind_tree_elements (
        tree_element_id bigint not null auto_increment,
        title varchar(255),
        topic bigint,
        lft integer,
        rgt integer,
        left_map_id bigint,
        right_map_id bigint,
        primary key (tree_element_id)
    ) type=InnoDB;

    create table occurrences (
        occurrence_id bigint not null auto_increment,
        discriminator varchar(255) not null,
        user_id bigint,
        title varchar(255),
        data text,
        dateUpdated timestamp,
        dateCreated timestamp,
        MindTree bigint,
        uri varchar(255),
        primary key (occurrence_id)
    ) type=InnoDB;

    create table subjects (
        subject_id bigint not null auto_increment,
        discriminator varchar(255) not null,
        foreignID varchar(255),
        name varchar(255),
        primary key (subject_id)
    ) type=InnoDB;

    create table topic_associations (
        association_id bigint not null,
        topic_id bigint not null,
        primary key (association_id, topic_id)
    ) type=InnoDB;

    create table topic_occurences (
        occurrence_id bigint not null,
        topic_id bigint not null,
        primary key (occurrence_id, topic_id)
    ) type=InnoDB;

    create table topic_scopes (
        topic_id bigint not null,
        scope_id bigint not null,
        primary key (topic_id, scope_id)
    ) type=InnoDB;

    create table typetable (
        from_id bigint not null,
        topic_id bigint not null,
        primary key (from_id, topic_id)
    ) type=InnoDB;

    alter table Topics 
        add index FK95A7C54424CD75BC (subject_id), 
        add constraint FK95A7C54424CD75BC 
        foreign key (subject_id) 
        references subjects (subject_id);

    alter table Topics 
        add index FK95A7C544BFBBD543 (user_id), 
        add constraint FK95A7C544BFBBD543 
        foreign key (user_id) 
        references Users (user_id);

    alter table Topics 
        add index FK95A7C544D7A8CDDA (subject), 
        add constraint FK95A7C544D7A8CDDA 
        foreign key (subject) 
        references subjects (subject_id);

    alter table instancetable 
        add index FKAEB83C59241F80B6 (from_id), 
        add constraint FKAEB83C59241F80B6 
        foreign key (from_id) 
        references Topics (topic_id);

    alter table instancetable 
        add index FKAEB83C59E7D2111 (topic_id), 
        add constraint FKAEB83C59E7D2111 
        foreign key (topic_id) 
        references Topics (topic_id);

    alter table member_topics 
        add index FKAF560A696E60CBA9 (member_topics_id), 
        add constraint FKAF560A696E60CBA9 
        foreign key (member_topics_id) 
        references Topics (topic_id);

    alter table member_topics 
        add index FKAF560A69978CE7C6 (member_id), 
        add constraint FKAF560A69978CE7C6 
        foreign key (member_id) 
        references Topics (topic_id);

    alter table mind_tree 
        add index FK2781EB6B4E230C15 (topic), 
        add constraint FK2781EB6B4E230C15 
        foreign key (topic) 
        references Topics (topic_id);

    alter table mind_tree_elements 
        add index FK440EAF4B1484FB6A (left_map_id), 
        add constraint FK440EAF4B1484FB6A 
        foreign key (left_map_id) 
        references mind_tree (map_id);

    alter table mind_tree_elements 
        add index FK440EAF4BD40E47B5 (right_map_id), 
        add constraint FK440EAF4BD40E47B5 
        foreign key (right_map_id) 
        references mind_tree (map_id);

    alter table mind_tree_elements 
        add index FK440EAF4B4E230C15 (topic), 
        add constraint FK440EAF4B4E230C15 
        foreign key (topic) 
        references Topics (topic_id);

    alter table occurrences 
        add index FK2EC250C2B830444 (MindTree), 
        add constraint FK2EC250C2B830444 
        foreign key (MindTree) 
        references mind_tree (map_id);

    alter table occurrences 
        add index FK2EC250C2BFBBD543 (user_id), 
        add constraint FK2EC250C2BFBBD543 
        foreign key (user_id) 
        references Users (user_id);

    alter table topic_associations 
        add index FKD04875225C01F87F (association_id), 
        add constraint FKD04875225C01F87F 
        foreign key (association_id) 
        references Topics (topic_id);

    alter table topic_associations 
        add index FKD048752227AACB23 (topic_id), 
        add constraint FKD048752227AACB23 
        foreign key (topic_id) 
        references Topics (topic_id);

    alter table topic_occurences 
        add index FK6F7A6BACCEFD292F (occurrence_id), 
        add constraint FK6F7A6BACCEFD292F 
        foreign key (occurrence_id) 
        references Topics (topic_id);

    alter table topic_occurences 
        add index FK6F7A6BAC479BD4A5 (topic_id), 
        add constraint FK6F7A6BAC479BD4A5 
        foreign key (topic_id) 
        references occurrences (occurrence_id);

    alter table topic_scopes 
        add index FK534459AF2A7FE1CC (scope_id), 
        add constraint FK534459AF2A7FE1CC 
        foreign key (scope_id) 
        references Topics (topic_id);

    alter table topic_scopes 
        add index FK534459AFE7D2111 (topic_id), 
        add constraint FK534459AFE7D2111 
        foreign key (topic_id) 
        references Topics (topic_id);

    alter table typetable 
        add index FK201420D4241F80B6 (from_id), 
        add constraint FK201420D4241F80B6 
        foreign key (from_id) 
        references Topics (topic_id);

    alter table typetable 
        add index FK201420D4E7D2111 (topic_id), 
        add constraint FK201420D4E7D2111 
        foreign key (topic_id) 
        references Topics (topic_id);
