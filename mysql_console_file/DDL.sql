CREATE TABLE User(
    Username varchar(45) PRIMARY KEY,
    Password varchar(45) not null,
    Email varchar(255) not null,
    Point integer not null DEFAULT 0 CHECK (Point>=0),
    IsBlocked boolean not null DEFAULT FALSE,
    CONSTRAINT mailcheck CHECK (regexp_like(Email,'^(\\S+)\\@(\\S+).(\\S+)$'))
);

CREATE TABLE Product(
    Name varchar(45) PRIMARY KEY ,
    Date DATE not null default (CURRENT_DATE),
    Image longblob not null
);

CREATE TABLE Questionnaire(
    ID integer NOT NULL auto_increment,
    ProductName varchar(45) not null,
    Username varchar(45) not null,
    Datetime TIMESTAMP not null default CURRENT_TIMESTAMP(),
    Age integer,
    Sex char check (Sex in ('M','F')),
    ExpertiseLevel varchar(8) check (ExpertiseLevel in ('LOW','MEDIUM','HIGH')),
    PRIMARY KEY (ID, ProductName, Username),
    FOREIGN KEY (ProductName) references Product(Name)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE,
    FOREIGN KEY (Username) references User(Username)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE
);

CREATE TABLE MarketingQuestion(
    Question varchar(512),
    ProductName varchar(45),
    PRIMARY KEY (Question, ProductName),
    FOREIGN KEY (ProductName) references Product(Name)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE
);

CREATE TABLE ContainMarketing(
    IDQues integer,
    ProductName varchar(45),
    MarketingQuestion varchar(512),
    Answer varchar(512) not null,
    PRIMARY KEY (IDQues, ProductName, MarketingQuestion),
    FOREIGN KEY (IDQues) references Questionnaire(ID)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE,
    FOREIGN KEY (ProductName) references Product(Name)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE,
    FOREIGN KEY (MarketingQuestion) references MarketingQuestion(Question)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE
);

CREATE TABLE Administrator(
    Name varchar(45) PRIMARY KEY,
    Password varchar(45)
);

CREATE TABLE OffensiveWord(
    OffensiveWord varchar(45) PRIMARY KEY
);

/*
 The user’s account is blocked,
 so that no questionnaires can be filled in by such account in the future.
 */
CREATE TRIGGER AccountIsBlocked1
BEFORE insert on Questionnaire FOR EACH ROW
BEGIN
    if (SELECT IsBlocked FROM User WHERE Username = NEW.Username) is True
    then SIGNAL SQLSTATE '45000'  SET MESSAGE_TEXT = 'Your account is blocked';
    end if;
END;


/*
 If any response of the user contains a word listed in the table,
 the transaction is rolled back, no data are recorded in the database,
 and the user’s account is blocked
 so that no questionnaires can be filled in by such account in the future.
 */
CREATE TRIGGER AccountIsBlocked2
BEFORE insert on ContainMarketing FOR EACH ROW
BEGIN
    if TRUE in (SELECT IsBlocked FROM User
        WHERE Username in (SELECT Username FROM Questionnaire
        WHERE ID = NEW.IDQues and ProductName = NEW.ProductName))
    then
        SIGNAL SQLSTATE '45000'  SET MESSAGE_TEXT = 'Your account is blocked';
    else
        if (SELECT COUNT(*) FROM OffensiveWord WHERE NEW.Answer REGEXP CONCAT('\(',OffensiveWord,'\)')) > 0
        then
            UPDATE User
                SET IsBlocked = TRUE WHERE Username in (SELECT Username FROM Questionnaire
                WHERE ID = NEW.IDQues and ProductName = NEW.ProductName);

            -- rolled back the transaction
            DELETE from Questionnaire
                WHERE ID = NEW.IDQues and ProductName = NEW.ProductName;

            SIGNAL SQLSTATE '45000'  SET MESSAGE_TEXT = 'Your account is blocked';
        end if;
    end if;
END;


/*
 One point is assigned for every answered question of section 1
 */
CREATE TRIGGER AddPointsForMarketingSection
AFTER insert on ContainMarketing FOR EACH ROW
BEGIN
    UPDATE User
        SET Point = Point + 1
    WHERE Username in (SELECT Username FROM Questionnaire
            WHERE ID = NEW.IDQues and ProductName = NEW.ProductName);
END;


/*
 Two points are assigned for every answered optional question of section 2
 */
CREATE TRIGGER AddPointsForStatisticalSection
AFTER insert on Questionnaire FOR EACH ROW
BEGIN
    IF NEW.Sex is not null
    then
        UPDATE User
            SET Point = Point + 2
        WHERE Username = NEW.Username;
    end if;
    IF NEW.Age is not null
    then
        UPDATE User
            SET Point = Point + 2
        WHERE Username = NEW.Username;
    end if;
    IF NEW.ExpertiseLevel is not null
    then
        UPDATE User
            SET Point = Point + 2
        WHERE Username = NEW.Username;
    end if;
END;


/*
    A CREATION page for inserting the product of the day for the current date or for a posterior date
 */
CREATE TRIGGER OnlyCreateProductForCurrentORPosteriorDate
BEFORE insert on Product FOR EACH ROW
BEGIN
    if NEW.Date < CURRENT_DATE()
    then
        SIGNAL SQLSTATE '45000'  SET MESSAGE_TEXT = 'You can only inserting
                    the product for the current date or for a posterior date';
    end if;
END;

/*
A DELETION page for ERASING the questionnaire data and
the related responses and points of all users who filled in the questionnaire.
Deletion should be possible only for a date preceding the current date.
*/
CREATE TRIGGER DeletionQuestionnaireOnlyCurrentDate
BEFORE delete on Questionnaire FOR EACH ROW
BEGIN
    if DATE(OLD.Datetime) <> CURRENT_DATE
    then
        SIGNAL SQLSTATE '45000'  SET MESSAGE_TEXT = 'Deletion questionnaire should be possible only for a date
                                                    preceding the current date.';
    else
        if OLD.Sex is not null
        then
            UPDATE User
                SET Point = Point - 2
            WHERE Username = OLD.Username;
        end if;

        if OLD.Age is not null
        then
            UPDATE User
                SET Point = Point - 2
            WHERE Username = OLD.Username;
        end if;

        if OLD.ExpertiseLevel is not null
        then
            UPDATE User
                SET Point = Point - 2
            WHERE Username = OLD.Username;
        end if;

        UPDATE User
            SET Point = Point - (SELECT count(*)
                                    FROM ContainMarketing
                                    WHERE OLD.ID = ContainMarketing.IDQues
                                        and OLD.ProductName = ContainMarketing.ProductName)
            WHERE Username = OLD.Username;
    end if;
END;

