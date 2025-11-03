
    create table _user (
        enabled boolean,
        create_at timestamp(6),
        last_login timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        email varchar(255) not null unique,
        password varchar(255),
        role varchar(255) check (role in ('USER','SCHOOL','ORGANIZATION','LECTURER','SUB_ADMIN','ADMIN')),
        primary key (id)
    );

    create table application_modules (
        application_id uuid not null,
        id uuid not null,
        module_id uuid not null,
        primary key (id)
    );

    create table applications (
        expected_salary numeric(38,2),
        status smallint check (status between 0 and 4),
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        lecturer_id uuid,
        project_id uuid,
        cover_letter TEXT,
        cv_url varchar(255),
        primary key (id)
    );

    create table attended_training_course (
        end_date date,
        number_of_hour integer,
        start_date date,
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        lecturer_id uuid,
        admin_note varchar(255),
        course_type varchar(255) check (course_type in ('FORMAL','SPECIALIZED','EXTRACURRICULAR')),
        course_url varchar(255),
        description varchar(255),
        location varchar(255),
        organizer varchar(255),
        scale varchar(255) check (scale in ('INSTITUTIONAL','UNIVERSITY','DEPARTMENTAL','MINISTERIAL','NATIONAL','INTERNATIONAL','OTHERS')),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        title varchar(255),
        topic varchar(255),
        primary key (id)
    );

    create table attended_training_course_update (
        end_date date,
        number_of_hour integer,
        start_date date,
        create_at timestamp(6),
        update_at timestamp(6),
        course_id uuid not null unique,
        id uuid not null,
        admin_note varchar(255),
        course_type varchar(255) check (course_type in ('FORMAL','SPECIALIZED','EXTRACURRICULAR')),
        course_url varchar(255),
        description varchar(255),
        location varchar(255),
        organizer varchar(255),
        scale varchar(255) check (scale in ('INSTITUTIONAL','UNIVERSITY','DEPARTMENTAL','MINISTERIAL','NATIONAL','INTERNATIONAL','OTHERS')),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        title varchar(255),
        topic varchar(255),
        primary key (id)
    );

    create table certification (
        expiry_date date,
        issue_date date,
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        lecturer_id uuid,
        admin_note varchar(255),
        certificate_url varchar(255),
        description varchar(255),
        issued_by varchar(255),
        level varchar(255),
        name varchar(255),
        reference_id varchar(255) not null,
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        primary key (id)
    );

    create table certification_update (
        expiry_date date,
        issue_date date,
        create_at timestamp(6),
        update_at timestamp(6),
        certification_id uuid not null unique,
        id uuid not null,
        admin_note varchar(255),
        certificate_url varchar(255),
        description varchar(255),
        issued_by varchar(255),
        level varchar(255),
        name varchar(255),
        reference_id varchar(255) not null,
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        primary key (id)
    );

    create table contracts (
        status smallint check (status between 0 and 3),
        create_at timestamp(6),
        signed_date timestamp(6),
        update_at timestamp(6),
        application_id uuid unique,
        id uuid not null,
        contract_no varchar(255),
        contract_url varchar(255),
        primary key (id)
    );

    create table course_infos (
        is_online boolean,
        level smallint check (level between 0 and 2),
        price numeric(38,2),
        published boolean not null,
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        project_id uuid unique,
        public_title varchar(500),
        thumbnail_url varchar(1000),
        address varchar(255),
        introduce TEXT,
        public_description TEXT,
        primary key (id)
    );

    create table course_module_requirements (
        course_module_id uuid not null,
        requirement varchar(255)
    );

    create table course_modules (
        duration integer,
        module_order integer,
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        project_id uuid,
        title varchar(500),
        description varchar(2000),
        primary key (id)
    );

    create table degree (
        graduation_year integer,
        start_year integer,
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        lecturer_id uuid,
        admin_note varchar(255),
        description varchar(255),
        institution varchar(255),
        level varchar(255),
        major varchar(255),
        name varchar(255),
        reference_id varchar(255) not null,
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        url varchar(255),
        primary key (id)
    );

    create table degree_update (
        graduation_year integer,
        start_year integer,
        create_at timestamp(6),
        update_at timestamp(6),
        degree_id uuid not null unique,
        id uuid not null,
        admin_note varchar(255),
        description varchar(255),
        institution varchar(255),
        level varchar(255),
        major varchar(255),
        name varchar(255),
        reference_id varchar(255) not null,
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        url varchar(255),
        primary key (id)
    );

    create table education_institution (
        established_year integer,
        hidden boolean not null,
        create_at timestamp(6),
        update_at timestamp(6),
        business_registration_number varchar(10) not null unique,
        id uuid not null,
        user_id uuid not null unique,
        address varchar(255),
        admin_note varchar(255),
        description varchar(255),
        institution_name varchar(255),
        institution_type varchar(255) check (institution_type in ('UNIVERSITY','TRAINING_CENTER')),
        logo_url varchar(255),
        phone_number varchar(255),
        position varchar(255),
        representative_name varchar(255),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        website varchar(255),
        primary key (id)
    );

    create table education_institution_update (
        established_year integer,
        create_at timestamp(6),
        update_at timestamp(6),
        education_institution_id uuid not null unique,
        id uuid not null,
        address varchar(255),
        admin_note varchar(255),
        business_registration_number varchar(255) not null unique,
        description varchar(255),
        institution_name varchar(255),
        institution_type varchar(255) check (institution_type in ('UNIVERSITY','TRAINING_CENTER')),
        logo_url varchar(255),
        phone_number varchar(255),
        position varchar(255),
        representative_name varchar(255),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        website varchar(255),
        primary key (id)
    );

    create table interview (
        score integer,
        create_at timestamp(6),
        interview_date timestamp(6) not null,
        update_at timestamp(6),
        application_id uuid,
        id uuid not null,
        feedback TEXT,
        location varchar(255) not null,
        mode varchar(255) not null check (mode in ('ONLINE','OFFLINE')),
        result varchar(255) check (result in ('PASS','FAIL','PENDING')),
        status varchar(255) check (status in ('SCHEDULED','COMPLETED','CANCELED')),
        primary key (id)
    );

    create table knowledges (
        course_info_id uuid not null,
        knowledge varchar(255)
    );

    create table lecturer (
        date_of_birth date,
        experience_years integer,
        gender boolean not null,
        hidden boolean not null,
        create_at timestamp(6),
        update_at timestamp(6),
        citizen_id varchar(12) not null unique,
        id uuid not null,
        user_id uuid not null unique,
        academic_rank varchar(255) check (academic_rank in ('KS','CN','THS','TS','PGS','GS')),
        address TEXT,
        admin_note varchar(255),
        avatar_url varchar(255),
        bio TEXT,
        full_name varchar(255) not null,
        job_field TEXT,
        lecturer_id varchar(255) unique,
        phone_number varchar(255),
        specialization varchar(255),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        primary key (id)
    );

    create table lecturer_update (
        date_of_birth date,
        experience_years integer,
        gender boolean,
        create_at timestamp(6),
        update_at timestamp(6),
        citizen_id varchar(12) not null unique,
        id uuid not null,
        lecturer_id uuid not null unique,
        academic_rank varchar(255) check (academic_rank in ('KS','CN','THS','TS','PGS','GS')),
        address TEXT,
        admin_note varchar(255),
        avatar_url varchar(255),
        bio TEXT,
        full_name varchar(255),
        job_field TEXT,
        phone_number varchar(255),
        specialization varchar(255),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        primary key (id)
    );

    create table owned_training_course (
        end_date date,
        is_online boolean,
        price float(53),
        start_date date,
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        lecturer_id uuid,
        address varchar(255),
        admin_note varchar(255),
        content_url varchar(255),
        course_type varchar(255) check (course_type in ('FORMAL','SPECIALIZED','EXTRACURRICULAR')),
        course_url varchar(255),
        description varchar(255),
        language varchar(255),
        level varchar(255),
        requirements varchar(255),
        scale varchar(255) check (scale in ('INSTITUTIONAL','UNIVERSITY','DEPARTMENTAL','MINISTERIAL','NATIONAL','INTERNATIONAL','OTHERS')),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        thumbnail_url varchar(255),
        title varchar(255),
        topic varchar(255),
        primary key (id)
    );

    create table owned_training_course_update (
        end_date date,
        is_online boolean,
        price float(53),
        start_date date,
        create_at timestamp(6),
        update_at timestamp(6),
        course_id uuid not null unique,
        id uuid not null,
        address varchar(255),
        admin_note varchar(255),
        content_url varchar(255),
        course_type varchar(255) check (course_type in ('FORMAL','SPECIALIZED','EXTRACURRICULAR')),
        course_url varchar(255),
        description varchar(255),
        language varchar(255),
        level varchar(255),
        requirements varchar(255),
        scale varchar(255) check (scale in ('INSTITUTIONAL','UNIVERSITY','DEPARTMENTAL','MINISTERIAL','NATIONAL','INTERNATIONAL','OTHERS')),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        thumbnail_url varchar(255),
        title varchar(255),
        topic varchar(255),
        primary key (id)
    );

    create table partner_organization (
        established_year integer,
        hidden boolean not null,
        create_at timestamp(6),
        update_at timestamp(6),
        business_registration_number varchar(10) not null unique,
        id uuid not null,
        user_id uuid not null unique,
        address varchar(255),
        admin_note varchar(255),
        description TEXT,
        industry varchar(255),
        logo_url varchar(255),
        organization_name varchar(255),
        phone_number varchar(255),
        position varchar(255),
        representative_name varchar(255),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        website varchar(255),
        primary key (id)
    );

    create table partner_organization_update (
        established_year integer,
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        partner_organization_id uuid not null unique,
        address varchar(255),
        admin_note varchar(255),
        business_registration_number varchar(255) not null unique,
        description TEXT,
        industry varchar(255),
        logo_url varchar(255),
        organization_name varchar(255),
        phone_number varchar(255),
        position varchar(255),
        representative_name varchar(255),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        website varchar(255),
        primary key (id)
    );

    create table project_benefits (
        id uuid not null,
        benefit varchar(255)
    );

    create table project_requirements (
        id uuid not null,
        requirement varchar(255)
    );

    create table projects (
        budget numeric(38,2),
        duration integer,
        end_date date,
        is_remote boolean,
        member_count integer,
        published boolean not null,
        start_date date,
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        institution_id uuid,
        partner_id uuid,
        location varchar(500),
        title varchar(500),
        description varchar(1000),
        duration_unit varchar(255),
        field varchar(255),
        job_description TEXT,
        status varchar(255) check (status in ('PREPARE','REVIEW','PROCESS','SUCCESS','COMPLETED')),
        type varchar(255) check (type in ('RESEARCH','COURSE')),
        primary key (id)
    );

    create table requirements (
        course_info_id uuid not null,
        requirement varchar(255)
    );

    create table research_project (
        end_date date,
        founding_amount float(53),
        start_date date,
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        lecturer_id uuid,
        admin_note varchar(255),
        course_status varchar(255),
        description varchar(255),
        founding_source varchar(255),
        project_type varchar(255) check (project_type in ('RESEARCH','TOPIC','PROJECT')),
        published_url varchar(255),
        research_area varchar(255),
        role_in_project varchar(255),
        scale varchar(255) check (scale in ('INSTITUTIONAL','UNIVERSITY','DEPARTMENTAL','MINISTERIAL','NATIONAL','INTERNATIONAL','OTHERS')),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        title varchar(255),
        primary key (id)
    );

    create table research_project_update (
        end_date date,
        founding_amount float(53),
        start_date date,
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        research_project_id uuid not null unique,
        admin_note varchar(255),
        course_status varchar(255),
        description varchar(255),
        founding_source varchar(255),
        project_type varchar(255) check (project_type in ('RESEARCH','TOPIC','PROJECT')),
        published_url varchar(255),
        research_area varchar(255),
        role_in_project varchar(255),
        scale varchar(255) check (scale in ('INSTITUTIONAL','UNIVERSITY','DEPARTMENTAL','MINISTERIAL','NATIONAL','INTERNATIONAL','OTHERS')),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        title varchar(255),
        primary key (id)
    );

    create table sub_admin_permission (
        created_at timestamp(6),
        assigned_by uuid not null,
        id uuid not null,
        user_id uuid not null,
        permission varchar(255) not null check (permission in ('ORGANIZATION_READ','ORGANIZATION_CREATE','ORGANIZATION_UPDATE','ORGANIZATION_DELETE','ORGANIZATION_APPROVE','LECTURER_READ','LECTURER_CREATE','LECTURER_UPDATE','LECTURER_DELETE','LECTURER_APPROVE','SCHOOL_READ','SCHOOL_CREATE','SCHOOL_UPDATE','SCHOOL_DELETE','SCHOOL_APPROVE','COURSE_READ','COURSE_CREATE','COURSE_UPDATE','COURSE_DELETE','PROGRAM_READ','PROGRAM_CREATE','PROGRAM_UPDATE','PROGRAM_ARCHIVE')),
        primary key (id)
    );

    create table sub_emails (
        user_id uuid not null,
        sub_email varchar(255) not null unique,
        primary key (user_id, sub_email)
    );

    create table training_program (
        duration_hours integer,
        duration_sessions integer,
        end_date date,
        internal_price numeric(38,2),
        listed_price numeric(38,2),
        max_students integer,
        min_students integer,
        price_visible boolean not null,
        program_level smallint check (program_level between 0 and 2),
        public_price numeric(38,2),
        rating float(53),
        start_date date,
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        partner_organization_id uuid,
        training_program_request_id uuid unique,
        user_id uuid,
        banner_url varchar(255),
        certificate_issuer TEXT,
        classroom_link TEXT,
        completion_certificate_type TEXT,
        content_url varchar(255),
        description TEXT,
        equipment_requirement TEXT,
        learning_objectives TEXT,
        learning_outcomes TEXT,
        opening_condition TEXT,
        program_mode varchar(255) check (program_mode in ('ONLINE','OFFLINE','HYBRID')),
        program_status varchar(255) check (program_status in ('REVIEW','PUBLISHED','UNLISTED','ARCHIVED')),
        program_type varchar(255) check (program_type in ('SINGLE','PATHWAY','ENTERPRISE_TOPIC')),
        requirements TEXT,
        scale TEXT,
        schedule_detail TEXT,
        short_description TEXT,
        sub_title TEXT,
        syllabus_file_url varchar(255),
        target_audience TEXT,
        title TEXT,
        training_program_id varchar(255),
        primary key (id)
    );

    create table training_program_request (
        create_at timestamp(6),
        update_at timestamp(6),
        id uuid not null,
        partner_organization_id uuid,
        description TEXT,
        file_url varchar(255),
        status varchar(255) check (status in ('PENDING','APPROVED','REJECTED')),
        title TEXT,
        primary key (id)
    );

    create table training_program_tags (
        id uuid not null,
        tag varchar(255)
    );

    create table training_unit (
        duration_section integer,
        lead boolean not null,
        order_section integer,
        id uuid not null,
        lecturer_id uuid,
        training_program_id uuid,
        description TEXT,
        title TEXT,
        primary key (id)
    );

    alter table if exists application_modules 
       add constraint FKks72pox1jtx46j2wscmly34by 
       foreign key (application_id) 
       references applications;

    alter table if exists application_modules 
       add constraint FK3ox3xtss93pni3aairle7rtrv 
       foreign key (module_id) 
       references course_modules;

    alter table if exists applications 
       add constraint FK5wnagpur6eidf4sntaxxsptgj 
       foreign key (lecturer_id) 
       references lecturer;

    alter table if exists applications 
       add constraint FKhm18k2os8y6nqh80nv10g5cb6 
       foreign key (project_id) 
       references projects;

    alter table if exists attended_training_course 
       add constraint FKv3ih18x9n2ywhhphs6atbbhf 
       foreign key (lecturer_id) 
       references lecturer;

    alter table if exists attended_training_course_update 
       add constraint FKnxv22vh7tgjvhmy7fb6ppsc48 
       foreign key (course_id) 
       references attended_training_course;

    alter table if exists certification 
       add constraint FKcb7l2cjqywideuu0vxpuotjr 
       foreign key (lecturer_id) 
       references lecturer;

    alter table if exists certification_update 
       add constraint FKksh7t1c0t93v2m0gkpbuaya0s 
       foreign key (certification_id) 
       references certification;

    alter table if exists contracts 
       add constraint FKiv6pv5b1owqgdla333vo3onh6 
       foreign key (application_id) 
       references applications;

    alter table if exists course_infos 
       add constraint FK38ymdbt0juc1nu2mo5aqv0fwx 
       foreign key (project_id) 
       references projects;

    alter table if exists course_module_requirements 
       add constraint FK6kdsfgqu527g6fpbt3kits9sm 
       foreign key (course_module_id) 
       references course_modules;

    alter table if exists course_modules 
       add constraint FKnrhjvft951fskmpfp9vfo82ps 
       foreign key (project_id) 
       references projects;

    alter table if exists degree 
       add constraint FKacjusoj2v9ebtlekmpnysne1m 
       foreign key (lecturer_id) 
       references lecturer;

    alter table if exists degree_update 
       add constraint FKer1k8lc2gbu8g9gjyka0tc459 
       foreign key (degree_id) 
       references degree;

    alter table if exists education_institution 
       add constraint FKcojsh2ui0dbc36p2ann5i57qd 
       foreign key (user_id) 
       references _user;

    alter table if exists education_institution_update 
       add constraint FKsxcab46jj8x0vvtvbpj76f27k 
       foreign key (education_institution_id) 
       references education_institution;

    alter table if exists interview 
       add constraint FK83ira5y3yd25iclin92mljwgw 
       foreign key (application_id) 
       references applications;

    alter table if exists knowledges 
       add constraint FKq6lskbjv8vlku87l0tj3an97d 
       foreign key (course_info_id) 
       references course_infos;

    alter table if exists lecturer 
       add constraint FKd0tt5g3g2oomfss4hw03hh4vc 
       foreign key (user_id) 
       references _user;

    alter table if exists lecturer_update 
       add constraint FK3oy3991dly38630f26gx8pikd 
       foreign key (lecturer_id) 
       references lecturer;

    alter table if exists owned_training_course 
       add constraint FK3k6h1na1ffmr6av5xt4n6rl0w 
       foreign key (lecturer_id) 
       references lecturer;

    alter table if exists owned_training_course_update 
       add constraint FKmtv7ny7lxd42x33u42urg0yn4 
       foreign key (course_id) 
       references owned_training_course;

    alter table if exists partner_organization 
       add constraint FKmt1w7ngs9mo8ahjmbbr7eink5 
       foreign key (user_id) 
       references _user;

    alter table if exists partner_organization_update 
       add constraint FKhu2d4sh7t5ohfiwlj9xofawsd 
       foreign key (partner_organization_id) 
       references partner_organization;

    alter table if exists project_benefits 
       add constraint FKllbqqq9v5p212qyer5f3cesih 
       foreign key (id) 
       references projects;

    alter table if exists project_requirements 
       add constraint FKm6qppubm4tgje8c03ci20lchm 
       foreign key (id) 
       references projects;

    alter table if exists projects 
       add constraint FK999gi5timvb5k6b2dfq70ha3e 
       foreign key (institution_id) 
       references education_institution;

    alter table if exists projects 
       add constraint FKqdeavpldul4gsnkt23j0ekq2i 
       foreign key (partner_id) 
       references partner_organization;

    alter table if exists requirements 
       add constraint FK3m2on8qaqebol9qney71dbn0u 
       foreign key (course_info_id) 
       references course_infos;

    alter table if exists research_project 
       add constraint FKct07td0f8wyseyoq4nkakexlt 
       foreign key (lecturer_id) 
       references lecturer;

    alter table if exists research_project_update 
       add constraint FKjgv0mcvvdffk1vtw1llru8gff 
       foreign key (research_project_id) 
       references research_project;

    alter table if exists sub_admin_permission 
       add constraint FKr20a77329iygd00603y8dnd9w 
       foreign key (assigned_by) 
       references _user;

    alter table if exists sub_admin_permission 
       add constraint FKqem84hvfjaegdtbo3obrxyyc2 
       foreign key (user_id) 
       references _user;

    alter table if exists sub_emails 
       add constraint FKl6w7p1kpkgjkg9s9og8xluca3 
       foreign key (user_id) 
       references _user;

    alter table if exists training_program 
       add constraint FKg741x8kjdtmmvww558o041g29 
       foreign key (partner_organization_id) 
       references partner_organization;

    alter table if exists training_program 
       add constraint FKex4wgxpgd43t72e214rxmpddh 
       foreign key (training_program_request_id) 
       references training_program_request;

    alter table if exists training_program 
       add constraint FKhoyfcpo33ww3h7lnuc4ir0h03 
       foreign key (user_id) 
       references _user;

    alter table if exists training_program_request 
       add constraint FKo7bpq5ewfiq584b4t58esbnub 
       foreign key (partner_organization_id) 
       references partner_organization;

    alter table if exists training_program_tags 
       add constraint FKdqw5p51915divsmbiw0rtixkf 
       foreign key (id) 
       references training_program;

    alter table if exists training_unit 
       add constraint FKn0bgkslvam4xvdcn8d82hjdr6 
       foreign key (lecturer_id) 
       references lecturer;

    alter table if exists training_unit 
       add constraint FKr2gtf4toi9ae4kd675xdlibo2 
       foreign key (training_program_id) 
       references training_program;
