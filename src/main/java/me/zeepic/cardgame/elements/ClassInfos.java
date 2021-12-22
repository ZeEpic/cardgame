package me.zeepic.cardgame.elements;

public class ClassInfos {
//
//    static {
//        Classes.registerClass(new ClassInfo<>(Monster.class, "monster")
//                .user("monsters?")
//                .name("Monster")
//                .description("Represents a physical Monster card.")
//                .examples("on monster attack action:", "\tevent-monster's name is \"Angel\"", "\theal event-monster by 1")
//                .defaultExpression(new EventValueExpression<>(Monster.class))
//                .parser(new Parser<>() {
//
//                    @Override
//                    public Monster parse(String s, ParseContext context) {
//                        return super.parse(s, context);
//                    }
//
//                    @Override
//                    public boolean canParse(ParseContext context) {
//                        return super.canParse(context);
//                    }
//
//                    @Override
//                    public String toString(Monster o, int flags) {
//                        return o.getName() + " Monster at " + o.getLocation().getBlockX() + ", " + o.getLocation().getBlockZ();
//                    }
//
//                    @Override
//                    public String toVariableNameString(Monster o) {
//                        return o.getName().replace(" ", "_").toLowerCase() + "_monster_at_"
//                                + o.getLocation().getBlockX() + ", " + o.getLocation().getBlockZ();
//                    }
//
//                    @Override
//                    public String getVariableNamePattern() {
//                        return "(.*)_monster_at_[0-9],[0-9]";
//                    }
//
//                })
//                .serializer(new Serializer<>() {
//                    @Override
//                    public Fields serialize(Monster o) throws NotSerializableException {
//                        throw new NotSerializableException(Monster.class.getName());
//                    }
//
//                    @Override
//                    public void deserialize(Monster o, Fields f) throws NotSerializableException {
//                        throw new NotSerializableException(Monster.class.getName());
//                    }
//
//                    @Override
//                    public boolean mustSyncDeserialization() {
//                        return false;
//                    }
//
//                    @Override
//                    protected boolean canBeInstantiated() {
//                        return false;
//                    }
//                })
//        );
//    }

}
