package wint.lang.utils;

import junit.framework.TestCase;
import wint.lang.magic.Transformer;
import wint.lang.utils.merger.DestPool;
import wint.lang.utils.merger.DestPools;
import wint.lang.utils.merger.Mapper;

import java.util.Arrays;
import java.util.List;

/**
 * User: huangsongli
 * Date: 15/3/4
 * Time: 上午10:10
 */
public class CollectionUtilTest extends TestCase {

    private PersonAddressService personAddressService = new PersonAddressService();

    public void testMerge() throws Exception {
        List<Person> personList = Arrays.asList(new Person(1, "aa"), new Person(2, "bb"));
        List<PersonVO> personVOList = CollectionUtil.transformList(personList, new Transformer<Person, PersonVO>() {
            @Override
            public PersonVO transform(Person object) {
                PersonVO personVO = new PersonVO();
                personVO.setPerson(object);
                return personVO;
            }
        });

        CollectionUtil.merge(personVOList, new Mapper<PersonVO, Integer, Address>() {
            @Override
            public Integer toKey(PersonVO source) {
                return source.getPerson().getId();
            }

            @Override
            public DestPool<Integer, Address> toDestPool(List<Integer> keys) {
                List<Address> addressList = personAddressService.queryListByPersonIds(keys);
                return DestPools.toDestPool(addressList, new Transformer<Address, Integer>() {
                    @Override
                    public Integer transform(Address object) {
                        return object.getPersonId();
                    }
                });
            }

            @Override
            public void setDest(PersonVO source, Address dest) {
                source.setAddress(dest);
            }
        });


        System.out.println(personVOList);
    }

    private static class PersonAddressService {

        public List<Address> queryListByPersonIds(List<Integer> ids) {
            return Arrays.asList(new Address(1, "addr1"), new Address(2, "addr2"));
        }

    }

    private static class PersonVO {
        private Person person;
        private Address address;

        private Person getPerson() {
            return person;
        }

        private void setPerson(Person person) {
            this.person = person;
        }

        private Address getAddress() {
            return address;
        }

        private void setAddress(Address address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "PersonVO{" +
                    "person=" + person +
                    ", address=" + address +
                    '}';
        }
    }

    private static class Person {

        private int id;

        private String name;

        private Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        private int getId() {
            return id;
        }

        private void setId(int id) {
            this.id = id;
        }

        private String getName() {
            return name;
        }

        private void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    private static class Address {
        private int personId;
        private String address;

        @Override
        public String toString() {
            return "Address{" +
                    "personId=" + personId +
                    ", address='" + address + '\'' +
                    '}';
        }

        private Address(int personId, String address) {
            this.personId = personId;
            this.address = address;
        }

        private int getPersonId() {
            return personId;
        }

        private void setPersonId(int personId) {
            this.personId = personId;
        }

        private String getAddress() {
            return address;
        }

        private void setAddress(String address) {
            this.address = address;
        }
    }


}
